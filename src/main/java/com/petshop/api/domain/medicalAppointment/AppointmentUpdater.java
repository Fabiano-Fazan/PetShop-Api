package com.petshop.api.domain.medicalAppointment;


import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.update.UpdateMedicalAppointmentDto;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.repository.AnimalRepository;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.VeterinarianRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentUpdater {

    private final ClientRepository clientRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final AnimalRepository animalRepository;
    private final ValidatorEntities validatorEntities;
    private final AppointmentTimeCalculator timeCalculator;

    public void updateAppointment(MedicalAppointment medicalAppointment, @NonNull UpdateMedicalAppointmentDto updateDto){
        if(updateDto.getClientId() != null){
            medicalAppointment.setClient(validatorEntities.validate(updateDto.getClientId(), clientRepository, "Client"));
        }

        if(updateDto.getAnimalId() != null){
            medicalAppointment.setAnimal(validatorEntities.validate(updateDto.getAnimalId(), animalRepository, "Animal"));
        }

        if(updateDto.getVeterinarianId() != null){
            medicalAppointment.setVeterinarian(validatorEntities.validate(updateDto.getVeterinarianId(), veterinarianRepository, "Veterinarian"));

        }

        if(updateDto.getAppointmentStartTime() != null || updateDto.getDurationMinutes() != null){
            LocalDateTime start = timeCalculator.start(
                    updateDto.getAppointmentStartTime(),
                    medicalAppointment.getAppointmentStartTime()
            );

            int duration = timeCalculator.duration(
                    updateDto.getDurationMinutes(),
                    medicalAppointment.getDurationMinutes()
            );

        if (updateDto.getAppointmentStatus() != null) {
            medicalAppointment.setAppointmentStatus(updateDto.getAppointmentStatus());
        }
            LocalDateTime end = timeCalculator.end(start, duration);
            medicalAppointment.setAppointmentStartTime(start);
            medicalAppointment.setAppointmentEndTime(end);
            medicalAppointment.setDurationMinutes(duration);
            medicalAppointment.setNotes(updateDto.getNotes());
            medicalAppointment.setTreatment(updateDto.getTreatment());
        }
    }
}