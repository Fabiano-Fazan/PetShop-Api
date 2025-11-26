package com.petshop.api.domain.Validator;

import com.petshop.api.exception.AppointmentDateTimeAlreadyExistsException;
import com.petshop.api.repository.MedicalAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateAppointment {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    public void validateAppointmentTimeConflict(UUID veterinarianId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasConflict = medicalAppointmentRepository.existsConflictingAppointment(veterinarianId, startTime, endTime);
        if (hasConflict) {
            throw new AppointmentDateTimeAlreadyExistsException("This time slot is already booked for the veterinarian ID: " + veterinarianId);
        }
    }

}
