package com.petshop.api.service;

import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.request.UpdateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.exception.AppointmentDateTimeAlreadyExistsException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.model.entities.Veterinarian;
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
    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public Page<MedicalAppointmentResponseDto> getAllMedicalAppointments(Pageable pageable) {
        return medicalAppointmentRepository.findAll(pageable)
                .map(medicalAppointmentMapper::toResponseDto);
    }

    public MedicalAppointmentResponseDto getMedicalAppointmentById(UUID id) {
        MedicalAppointment medicalAppointmentById = medicalAppointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical appointment not found with ID: " + id));
        return medicalAppointmentMapper.toResponseDto(medicalAppointmentById);

    }

    @Transactional
    public MedicalAppointmentResponseDto createMedicalAppointment(CreateMedicalAppointmentDto createMedicalAppointmentDTO) {

        LocalDateTime start = createMedicalAppointmentDTO.getAppointmentStartTime();
        int duration = createMedicalAppointmentDTO.getDurationMinutes() != null ? createMedicalAppointmentDTO.getDurationMinutes() : 30;
        LocalDateTime end = start.plusMinutes(duration);

        boolean hasConflict = medicalAppointmentRepository.existsConflictingAppointment(createMedicalAppointmentDTO.getVeterinarianId(), start, end);
        if (hasConflict) {
            throw new AppointmentDateTimeAlreadyExistsException("This time slot is already booked for the veterinarian : " + createMedicalAppointmentDTO.getVeterinarianId());
        }

        Client client = clientRepository.findById(createMedicalAppointmentDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + createMedicalAppointmentDTO.getClientId()));

        Animal animal = animalRepository.findById(createMedicalAppointmentDTO.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with ID: " + createMedicalAppointmentDTO.getAnimalId()));

        Veterinarian veterinarian = veterinarianRepository.findById(createMedicalAppointmentDTO.getVeterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with ID: " + createMedicalAppointmentDTO.getVeterinarianId()));

        MedicalAppointment appointment = medicalAppointmentMapper.toEntity(createMedicalAppointmentDTO);
        appointment.setAppointmentEndTime(end);
        appointment.setClient(client);
        appointment.setAnimal(animal);
        appointment.setVeterinarian(veterinarian);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        MedicalAppointment savedAppointment = medicalAppointmentRepository.save(appointment);
        return medicalAppointmentMapper.toResponseDto(savedAppointment);
    }

    @Transactional
    public MedicalAppointmentResponseDto updateMedicalAppointment(UUID id, UpdateMedicalAppointmentDto updateMedicalAppointmentDto){

        MedicalAppointment medicalAppointment = medicalAppointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment  not found with ID: " + id));
        medicalAppointmentMapper.updateMedicalAppointmentDto(updateMedicalAppointmentDto, medicalAppointment);

        if (updateMedicalAppointmentDto.getVeterinarianId() != null){
            Veterinarian veterinarian = veterinarianRepository.findById(updateMedicalAppointmentDto.getVeterinarianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with ID: " + updateMedicalAppointmentDto.getVeterinarianId()));
        }

        if (updateMedicalAppointmentDto.getAnimalId() != null){
            Animal animal = animalRepository.findById(updateMedicalAppointmentDto.getAnimalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Animal not found with ID: " + updateMedicalAppointmentDto.getAnimalId()));
        }

        if (updateMedicalAppointmentDto.getClientId() != null){
            Client client = clientRepository.findById(updateMedicalAppointmentDto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + updateMedicalAppointmentDto.getClientId()));
        }

        if (updateMedicalAppointmentDto.getAppointmentStartTime() != null || updateMedicalAppointmentDto.getDurationMinutes() != null){

            LocalDateTime start = updateMedicalAppointmentDto.getAppointmentStartTime() != null
                    ? updateMedicalAppointmentDto.getAppointmentStartTime()
                    : medicalAppointment.getAppointmentStartTime();

            int duration = updateMedicalAppointmentDto.getDurationMinutes() != null
                    ? updateMedicalAppointmentDto.getDurationMinutes()
                    :medicalAppointment.getDurationMinutes();

            LocalDateTime end = start.plusMinutes(duration);

            boolean hasConflict = medicalAppointmentRepository.existsConflictingAppointment(medicalAppointment.getVeterinarian().getId(), start, end);
            if (hasConflict) {
                throw new AppointmentDateTimeAlreadyExistsException("This time slot is already booked for the veterinarian.");
            }

            medicalAppointment.setAppointmentStartTime(start);
            medicalAppointment.setAppointmentEndTime(end);
            medicalAppointment.setDurationMinutes(duration);
        }

        medicalAppointmentRepository.save(medicalAppointment);
        return medicalAppointmentMapper.toResponseDto(medicalAppointment);
    }

    @Transactional
    public void deleteMedicalAppointment(UUID id){
        if (!medicalAppointmentRepository.existsById(id)){
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        medicalAppointmentRepository.deleteById(id);
    }
}





