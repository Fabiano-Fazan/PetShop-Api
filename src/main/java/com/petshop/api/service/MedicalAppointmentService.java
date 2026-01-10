package com.petshop.api.service;

import com.petshop.api.domain.medicalAppointment.AppointmentTimeCalculator;
import com.petshop.api.domain.medicalAppointment.AppointmentUpdater;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.update.UpdateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.model.enums.AppointmentStatus;
import com.petshop.api.model.mapper.MedicalAppointmentMapper;
import com.petshop.api.repository.AnimalRepository;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.MedicalAppointmentRepository;
import com.petshop.api.repository.VeterinarianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalAppointmentService {
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final ValidatorEntities validatorEntities;
    private final AppointmentTimeCalculator timeCalculator;
    private final AppointmentUpdater updaterAppointment;


    public Page<MedicalAppointmentResponseDto> getAllMedicalAppointments(Pageable pageable) {
        return medicalAppointmentRepository.findAll(pageable)
                .map(medicalAppointmentMapper::toResponseDto);
    }

    public MedicalAppointmentResponseDto getMedicalAppointmentById(UUID id) {
        var medicalAppointment = validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment");
        return medicalAppointmentMapper.toResponseDto(medicalAppointment);
    }

    public Page<MedicalAppointmentResponseDto> getMedicalAppointmentsByVeterinarianNameContainingIgnoreCase(String name, Pageable pageable){
        return medicalAppointmentRepository.findByVeterinarianNameContainingIgnoreCase(name, pageable)
                .map(medicalAppointmentMapper::toResponseDto);
    }

    public Page<MedicalAppointmentResponseDto> getMedicalAppointmentsByClientNameContainingIgnoreCase(String name, Pageable pageable){
        return medicalAppointmentRepository.findByClientNameContainingIgnoreCase(name, pageable)
                .map(medicalAppointmentMapper::toResponseDto);
    }

    @Transactional
    public MedicalAppointmentResponseDto createMedicalAppointment(CreateMedicalAppointmentDto dto) {
        LocalDateTime start = dto.getAppointmentStartTime();
        LocalDateTime end = timeCalculator.end(start, dto.getDurationMinutes());
        timeCalculator.validateAppointmentTimeConflict(dto.getVeterinarianId(), start, end);
        var appointment = medicalAppointmentMapper.toEntity(dto);
        appointment.setAppointmentEndTime(end);
        appointment.setClient(validatorEntities.validate(dto.getClientId(), clientRepository, "Client"));
        appointment.setAnimal(validatorEntities.validate(dto.getAnimalId(), animalRepository, "Animal"));
        appointment.setVeterinarian(validatorEntities.validate(dto.getVeterinarianId(), veterinarianRepository, "Veterinarian"));
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        return medicalAppointmentMapper.toResponseDto(medicalAppointmentRepository.save(appointment));
    }

    @Transactional
    public MedicalAppointmentResponseDto updateMedicalAppointment(UUID id, UpdateMedicalAppointmentDto updateDto){
        var medicalAppointment = validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment");
        updaterAppointment.updateAppointment(medicalAppointment, updateDto);
        return medicalAppointmentMapper.toResponseDto(medicalAppointmentRepository.save(medicalAppointment));
    }

    @Transactional
    public void deleteMedicalAppointment(UUID id){
        CanDeleted(id);
        medicalAppointmentRepository.deleteById(id);
    }

    private void CanDeleted(UUID id) {
        MedicalAppointment appointment = validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment");
        if (appointment.getAppointmentStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException("Only appointments with status SCHEDULED can be deleted");
        }
    }
}





