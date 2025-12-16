package com.petshop.api.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateMedicalAppointmentDto {

    @NotNull(message = "Veterinarian ID is required")
    private UUID veterinarianId;

    @NotNull(message = "Animal ID is required")
    private UUID animalId;

    @NotNull(message = "Client ID ir required")
    private UUID clientId;

    @NotNull(message = "Appointment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentStartTime;

    private Integer durationMinutes;

    @Size(min = 5, max = 500, message = "The diagnosis must be between 5 and 500 characters")
    private String diagnosis;

    private String notes;

}

