package com.petshop.api.service;

import com.petshop.api.domain.medicalAppointment.AppointmentTimeCalculator;
import com.petshop.api.domain.medicalAppointment.AppointmentUpdater;
import com.petshop.api.domain.validator.ValidateAppointment;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.request.UpdateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.model.enums.AppointmentStatus;
import com.petshop.api.model.mapper.MedicalAppointmentMapper;
import com.petshop.api.repository.MedicalAppointmentRepository;
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
    private final ValidatorEntities validatorEntities;
    private final AppointmentTimeCalculator timeCalculator;
    private final AppointmentUpdater updaterAppointment;
    private final ValidateAppointment validateAppointment;


    public Page<MedicalAppointmentResponseDto> getAllMedicalAppointments(Pageable pageable) {
        return medicalAppointmentRepository.findAll(pageable)
                .map(medicalAppointmentMapper::toResponseDto);
    }

    public MedicalAppointmentResponseDto getMedicalAppointmentById(UUID id) {
        MedicalAppointment medicalAppointmentById = validatorEntities.validateMedicalAppointment(id);
        return medicalAppointmentMapper.toResponseDto(medicalAppointmentById);
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
    public MedicalAppointmentResponseDto createMedicalAppointment(CreateMedicalAppointmentDto createMedicalAppointmentDTO) {
        LocalDateTime start = createMedicalAppointmentDTO.getAppointmentStartTime();
        LocalDateTime end = timeCalculator.end(start, createMedicalAppointmentDTO.getDurationMinutes());
        validateAppointment.validateAppointmentTimeConflict(createMedicalAppointmentDTO.getVeterinarianId(), start, end);
        MedicalAppointment appointment = medicalAppointmentMapper.toEntity(createMedicalAppointmentDTO);
        appointment.setAppointmentEndTime(end);
        appointment.setClient(validatorEntities.validateClient(createMedicalAppointmentDTO.getClientId()));
        appointment.setAnimal(validatorEntities.validateAnimal(createMedicalAppointmentDTO.getAnimalId()));
        appointment.setVeterinarian(validatorEntities.validateVeterinarian(createMedicalAppointmentDTO.getVeterinarianId()));
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        return medicalAppointmentMapper.toResponseDto(medicalAppointmentRepository.save(appointment));
    }

    @Transactional
    public MedicalAppointmentResponseDto updateMedicalAppointment(UUID id, UpdateMedicalAppointmentDto updateMedicalAppointmentDto){
        MedicalAppointment medicalAppointment = validatorEntities.validateMedicalAppointment(id);
        updaterAppointment.updateAppointment(medicalAppointment, updateMedicalAppointmentDto);
        return medicalAppointmentMapper.toResponseDto(medicalAppointmentRepository.save(medicalAppointment));
    }

    @Transactional
    public void deleteMedicalAppointment(UUID id){
        if (!medicalAppointmentRepository.existsById(id)){
            throw new ResourceNotFoundException("Appointment not found");
        }
        if (!validatorEntities.validateMedicalAppointmentCanBeDeleted(id)){
            throw new BusinessException("Only appointments with status SCHEDULED can be deleted");
        }
        medicalAppointmentRepository.deleteById(id);
    }
}





