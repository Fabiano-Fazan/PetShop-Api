package com.petshop.api.domain.medicalAppointment;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentTimeCalculator {

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
}
