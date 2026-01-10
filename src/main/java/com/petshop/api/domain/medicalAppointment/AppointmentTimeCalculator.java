package com.petshop.api.domain.medicalAppointment;

import com.petshop.api.exception.AppointmentDateTimeAlreadyExistsException;
import com.petshop.api.repository.MedicalAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppointmentTimeCalculator {

    private final MedicalAppointmentRepository medicalAppointmentRepository;

    private static final int defaultDurationMinutes = 30;

    public int duration(Integer inputDuration,int defaultDuration){
        return inputDuration != null ? inputDuration : defaultDuration;
    }

    public LocalDateTime start(LocalDateTime inputStart, LocalDateTime defaultStart){
        return inputStart != null ? inputStart : defaultStart;
    }

    public LocalDateTime end(LocalDateTime start, Integer durationMinutes){
        int duration = durationMinutes != null ? durationMinutes : defaultDurationMinutes;
        return start.plusMinutes(duration);
    }

    public void validateAppointmentTimeConflict(UUID veterinarianId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasConflict = medicalAppointmentRepository.existsConflictingAppointment(veterinarianId, startTime, endTime);
        if (hasConflict) {
            throw new AppointmentDateTimeAlreadyExistsException("This time slot is already booked for this veterinarian");
        }
    }
}
