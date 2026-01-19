package com.petshop.api.service;

import com.petshop.api.domain.medicalAppointment.AppointmentTimeCalculator;
import com.petshop.api.domain.medicalAppointment.AppointmentUpdater;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.dto.update.UpdateMedicalAppointmentDto;
import com.petshop.api.exception.BusinessException;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentServiceTest {

    @InjectMocks
    private MedicalAppointmentService medicalAppointmentService;

    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private ValidatorEntities validatorEntities;

    @Mock
    private AppointmentTimeCalculator timeCalculator;

    @Mock
    private AppointmentUpdater updaterAppointment;


    @Test
    @DisplayName("Should return page of medical appointments")
    void getAllMedicalAppointments_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        MedicalAppointment appointment = new MedicalAppointment();
        MedicalAppointmentResponseDto responseDto = new MedicalAppointmentResponseDto();
        Page<MedicalAppointment> page = new PageImpl<>(List.of(appointment));

        when(medicalAppointmentRepository.findAll(pageable)).thenReturn(page);
        when(medicalAppointmentMapper.toResponseDto(appointment)).thenReturn(responseDto);

        Page<MedicalAppointmentResponseDto> result = medicalAppointmentService.getAllMedicalAppointments(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(medicalAppointmentRepository).findAll(pageable);
    }


    @Test
    @DisplayName("Should return medical appointment by ID when exists")
    void getMedicalAppointmentById_ShouldReturnDto() {

        UUID id = UUID.randomUUID();
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setId(id);
        MedicalAppointmentResponseDto responseDto = new MedicalAppointmentResponseDto();

        when(validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment")).thenReturn(appointment);
        when(medicalAppointmentMapper.toResponseDto(appointment)).thenReturn(responseDto);

        MedicalAppointmentResponseDto result = medicalAppointmentService.getMedicalAppointmentById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, medicalAppointmentRepository, "Medical Appointment");
    }


    @Test
    @DisplayName("Should search by veterinarian name")
    void getMedicalAppointmentsByVeterinarianName_ShouldReturnPage() {

        String name = "Dr. Pedro";
        Pageable pageable = PageRequest.of(0, 10);
        MedicalAppointment appointment = new MedicalAppointment();
        MedicalAppointmentResponseDto responseDto = new MedicalAppointmentResponseDto();
        Page<MedicalAppointment> page = new PageImpl<>(List.of(appointment));

        when(medicalAppointmentRepository.findByVeterinarianNameContainingIgnoreCase(name, pageable)).thenReturn(page);
        when(medicalAppointmentMapper.toResponseDto(appointment)).thenReturn(responseDto);

        Page<MedicalAppointmentResponseDto> result = medicalAppointmentService
                .getMedicalAppointmentsByVeterinarianNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should search by client name")
    void getMedicalAppointmentsByClientName_ShouldReturnPage() {

        String name = "Fabiano";
        Pageable pageable = PageRequest.of(0, 10);
        MedicalAppointment appointment = new MedicalAppointment();
        MedicalAppointmentResponseDto responseDto = new MedicalAppointmentResponseDto();
        Page<MedicalAppointment> page = new PageImpl<>(List.of(appointment));

        when(medicalAppointmentRepository.findByClientNameContainingIgnoreCase(name, pageable)).thenReturn(page);
        when(medicalAppointmentMapper.toResponseDto(appointment)).thenReturn(responseDto);

        Page<MedicalAppointmentResponseDto> result = medicalAppointmentService
                .getMedicalAppointmentsByClientNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should create medical appointment successfully")
    void createMedicalAppointment_ShouldCreate_WhenDataIsValid() {

        CreateMedicalAppointmentDto dto = new CreateMedicalAppointmentDto();
        dto.setAppointmentStartTime(LocalDateTime.now());
        dto.setDurationMinutes(30);
        dto.setVeterinarianId(UUID.randomUUID());
        dto.setClientId(UUID.randomUUID());
        dto.setAnimalId(UUID.randomUUID());
        LocalDateTime calculatedEnd = dto.getAppointmentStartTime().plusMinutes(30);
        MedicalAppointment entity = new MedicalAppointment();
        MedicalAppointment savedEntity = new MedicalAppointment();
        savedEntity.setId(UUID.randomUUID());
        Client client = new Client();
        Animal animal = new Animal();
        Veterinarian veterinarian = new Veterinarian();
        MedicalAppointmentResponseDto responseDto = new MedicalAppointmentResponseDto();

        when(timeCalculator.end(dto.getAppointmentStartTime(), dto.getDurationMinutes())).thenReturn(calculatedEnd);
        when(medicalAppointmentMapper.toEntity(dto)).thenReturn(entity);
        when(validatorEntities.validate(dto.getClientId(), clientRepository, "Client")).thenReturn(client);
        when(validatorEntities.validate(dto.getAnimalId(), animalRepository, "Animal")).thenReturn(animal);
        when(validatorEntities.validate(dto.getVeterinarianId(), veterinarianRepository, "Veterinarian")).thenReturn(veterinarian);
        when(medicalAppointmentRepository.save(entity)).thenReturn(savedEntity);
        when(medicalAppointmentMapper.toResponseDto(savedEntity)).thenReturn(responseDto);

        MedicalAppointmentResponseDto result = medicalAppointmentService.createMedicalAppointment(dto);

        assertThat(result).isNotNull();

        verify(timeCalculator).validateAppointmentTimeConflict(
                dto.getVeterinarianId(),
                dto.getAppointmentStartTime(),
                calculatedEnd
        );

        assertThat(entity.getAppointmentEndTime()).isEqualTo(calculatedEnd);
        assertThat(entity.getClient()).isEqualTo(client);
        assertThat(entity.getAnimal()).isEqualTo(animal);
        assertThat(entity.getVeterinarian()).isEqualTo(veterinarian);
        assertThat(entity.getAppointmentStatus()).isEqualTo(AppointmentStatus.SCHEDULED);

        verify(medicalAppointmentRepository).save(entity);
    }

    @Test
    @DisplayName("Should update medical appointment successfully")
    void updateMedicalAppointment_ShouldUpdate_WhenIdExists() {

        UUID id = UUID.randomUUID();
        UpdateMedicalAppointmentDto updateDto = new UpdateMedicalAppointmentDto();
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setId(id);
        MedicalAppointment savedAppointment = new MedicalAppointment();
        MedicalAppointmentResponseDto responseDto = new MedicalAppointmentResponseDto();

        when(validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment")).thenReturn(appointment);
        when(medicalAppointmentRepository.save(appointment)).thenReturn(savedAppointment);
        when(medicalAppointmentMapper.toResponseDto(savedAppointment)).thenReturn(responseDto);

        MedicalAppointmentResponseDto result = medicalAppointmentService.updateMedicalAppointment(id, updateDto);

        assertThat(result).isNotNull();
        verify(updaterAppointment).updateAppointment(appointment, updateDto);
        verify(medicalAppointmentRepository).save(appointment);
    }


    @Test
    @DisplayName("Should delete medical appointment when status is SCHEDULED")
    void deleteMedicalAppointment_ShouldDelete_WhenStatusIsScheduled() {
        UUID id = UUID.randomUUID();
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setId(id);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);

        when(validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment")).thenReturn(appointment);

        medicalAppointmentService.deleteMedicalAppointment(id);

        verify(medicalAppointmentRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw BusinessException when deleting appointment that is not SCHEDULED")
    void deleteMedicalAppointment_ShouldThrowException_WhenStatusIsNotScheduled() {

        UUID id = UUID.randomUUID();
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setId(id);
        appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);

        when(validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment")).thenReturn(appointment);

        assertThatThrownBy(() -> medicalAppointmentService.deleteMedicalAppointment(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Only appointments with status SCHEDULED can be deleted");

        verify(medicalAppointmentRepository, never()).deleteById(any());
    }
}