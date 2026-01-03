package com.petshop.api.domain.validator;

import com.petshop.api.exception.AppointmentDateTimeAlreadyExistsException;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.model.enums.AppointmentStatus;
import com.petshop.api.repository.MedicalAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidatorAppointment {

    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final ValidatorEntities validatorEntities;

    public void validateAppointmentTimeConflict(UUID veterinarianId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasConflict = medicalAppointmentRepository.existsConflictingAppointment(veterinarianId, startTime, endTime);
        if (hasConflict) {
            throw new AppointmentDateTimeAlreadyExistsException("This time slot is already booked for this veterinarian");
        }
    }

    public void validateMedicalAppointmentCanBeDeleted(UUID id) {
        MedicalAppointment appointment = validatorEntities.validate(id, medicalAppointmentRepository, "Medical Appointment");
        if (appointment.getAppointmentStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException("Only appointments with status SCHEDULED can be deleted");
        }
    }
}
