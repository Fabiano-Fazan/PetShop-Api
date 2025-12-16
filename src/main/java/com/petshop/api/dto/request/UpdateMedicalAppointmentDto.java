package com.petshop.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petshop.api.model.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateMedicalAppointmentDto {

    private UUID veterinarianId;
    private UUID animalId;
    private UUID clientId;

    @NotNull(message = "Appointment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentStartTime;

    private Integer durationMinutes;

    private AppointmentStatus appointmentStatus;

    @Size(min = 5, max = 500, message = "The diagnosis must be between 10 and 500 characters")
    private String diagnosis;

    @Size(min = 5, max = 700, message = "The treatment must be between 10 and 700 characters")
    private String treatment;

    private String notes;
}
