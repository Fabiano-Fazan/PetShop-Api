package com.petshop.api.domain.medicalAppointment;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.update.UpdateMedicalAppointmentDto;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.model.entities.Veterinarian;
import com.petshop.api.model.enums.AppointmentStatus;
import com.petshop.api.repository.AnimalRepository;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.VeterinarianRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentUpdaterTest {

    @InjectMocks
    private AppointmentUpdater appointmentUpdater;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ValidatorEntities validatorEntities;

    @Mock
    private AppointmentTimeCalculator timeCalculator;


    @Test
    @DisplayName("Should update entity relationships (Client, Animal, Veterinarian) when IDs are present in DTO")
    void updateAppointment_ShouldUpdateRelationships_WhenIdsPresent() {

        MedicalAppointment appointment = new MedicalAppointment();
        UpdateMedicalAppointmentDto updateDto = new UpdateMedicalAppointmentDto();
        updateDto.setClientId(UUID.randomUUID());
        updateDto.setAnimalId(UUID.randomUUID());
        updateDto.setVeterinarianId(UUID.randomUUID());
        Client client = new Client();
        Animal animal = new Animal();
        Veterinarian veterinarian = new Veterinarian();

        when(validatorEntities.validate(updateDto.getClientId(), clientRepository, "Client")).thenReturn(client);
        when(validatorEntities.validate(updateDto.getAnimalId(), animalRepository, "Animal")).thenReturn(animal);
        when(validatorEntities.validate(updateDto.getVeterinarianId(), veterinarianRepository, "Veterinarian")).thenReturn(veterinarian);

        appointmentUpdater.updateAppointment(appointment, updateDto);

        assertThat(appointment.getClient()).isEqualTo(client);
        assertThat(appointment.getAnimal()).isEqualTo(animal);
        assertThat(appointment.getVeterinarian()).isEqualTo(veterinarian);
        verifyNoInteractions(timeCalculator);
    }

    @Test
    @DisplayName("Should update time, duration and end time when time fields are present")
    void updateAppointment_ShouldUpdateTimeFields_WhenTimeIsProvided() {

        LocalDateTime oldStart = LocalDateTime.now();
        LocalDateTime newStart = LocalDateTime.now().plusDays(1);
        LocalDateTime calculatedEnd = newStart.plusMinutes(60);
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setAppointmentStartTime(oldStart);
        appointment.setDurationMinutes(30);
        UpdateMedicalAppointmentDto updateDto = new UpdateMedicalAppointmentDto();
        updateDto.setAppointmentStartTime(newStart);
        updateDto.setDurationMinutes(60);

        when(timeCalculator.start(newStart, oldStart)).thenReturn(newStart);
        when(timeCalculator.duration(60, 30)).thenReturn(60);
        when(timeCalculator.end(newStart, 60)).thenReturn(calculatedEnd);

        appointmentUpdater.updateAppointment(appointment, updateDto);

        assertThat(appointment.getAppointmentStartTime()).isEqualTo(newStart);
        assertThat(appointment.getDurationMinutes()).isEqualTo(60);
        assertThat(appointment.getAppointmentEndTime()).isEqualTo(calculatedEnd);
        verify(timeCalculator).start(any(), any());
        verify(timeCalculator).duration(any(), anyInt());
        verify(timeCalculator).end(any(), anyInt());
    }

    @Test
    @DisplayName("Should update details (Notes, Status, Treatment) independently of Time fields")
    void updateAppointment_ShouldUpdateDetails_WhenTimeFieldsAreNull() {

        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setAppointmentStartTime(LocalDateTime.now());
        appointment.setNotes("Old Note");
        UpdateMedicalAppointmentDto updateDto = new UpdateMedicalAppointmentDto();
        updateDto.setNotes("New Note Only");
        updateDto.setTreatment("New Treatment Only");
        updateDto.setAppointmentStatus(AppointmentStatus.COMPLETED);
        updateDto.setAppointmentStartTime(null);
        updateDto.setDurationMinutes(null);
        appointmentUpdater.updateAppointment(appointment, updateDto);

        assertThat(appointment.getNotes()).isEqualTo("New Note Only");
        assertThat(appointment.getTreatment()).isEqualTo("New Treatment Only");
        assertThat(appointment.getAppointmentStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        verifyNoInteractions(timeCalculator);
    }

    @Test
    @DisplayName("Should NOT update anything if DTO fields are completely null")
    void updateAppointment_ShouldNotUpdate_WhenDtoFieldsAreNull() {

        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setAppointmentStartTime(LocalDateTime.now());
        appointment.setDurationMinutes(30);
        appointment.setNotes("Original Note");
        UpdateMedicalAppointmentDto updateDto = new UpdateMedicalAppointmentDto();
        appointmentUpdater.updateAppointment(appointment, updateDto);

        verifyNoInteractions(validatorEntities);
        verifyNoInteractions(timeCalculator);

        assertThat(appointment.getNotes()).isEqualTo("Original Note");
        assertThat(appointment.getClient()).isNull();
    }
}