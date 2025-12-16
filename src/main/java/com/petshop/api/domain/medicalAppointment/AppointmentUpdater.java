package com.petshop.api.domain.medicalAppointment;


import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.UpdateMedicalAppointmentDto;
import com.petshop.api.model.entities.MedicalAppointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentUpdater {

    private final ValidatorEntities validatorEntities;
    private final AppointmentTimeCalculator timeCalculator;

    public void updateAppointment(MedicalAppointment medicalAppointment, UpdateMedicalAppointmentDto updateMedicalAppointmentDto){
        if(updateMedicalAppointmentDto.getClientId() != null){
            medicalAppointment.setClient(validatorEntities.validateClient(updateMedicalAppointmentDto.getClientId()));
        }

        if(updateMedicalAppointmentDto.getAnimalId() != null){
            medicalAppointment.setAnimal(validatorEntities.validateAnimal(updateMedicalAppointmentDto.getAnimalId()));
        }

        if(updateMedicalAppointmentDto.getVeterinarianId() != null){
            medicalAppointment.setVeterinarian(validatorEntities.validateVeterinarian(updateMedicalAppointmentDto.getVeterinarianId()));
        }

        if(updateMedicalAppointmentDto.getAppointmentStartTime() != null || updateMedicalAppointmentDto.getDurationMinutes() != null){
            LocalDateTime start = timeCalculator.start(
                    updateMedicalAppointmentDto.getAppointmentStartTime(),
                    medicalAppointment.getAppointmentStartTime()
            );

            int duration = timeCalculator.duration(
                    updateMedicalAppointmentDto.getDurationMinutes(),
                    medicalAppointment.getDurationMinutes()
            );

        if (updateMedicalAppointmentDto.getAppointmentStatus() != null) {
            medicalAppointment.setAppointmentStatus(updateMedicalAppointmentDto.getAppointmentStatus());
        }
            LocalDateTime end = timeCalculator.end(start, duration);
            medicalAppointment.setAppointmentStartTime(start);
            medicalAppointment.setAppointmentEndTime(end);
            medicalAppointment.setDurationMinutes(duration);
            medicalAppointment.setNotes(updateMedicalAppointmentDto.getNotes());
            medicalAppointment.setTreatment(updateMedicalAppointmentDto.getTreatment());
        }
    }
}