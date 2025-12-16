package com.petshop.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalAppointmentResponseDto {

    private UUID id;
    private LocalDateTime appointmentStartTime;
    private LocalDateTime appointmentEndTime;
    private String appointmentStatus;
    private String diagnosis;
    private String treatment;
    private String notes;

    private UUID veterinarianId;
    private String veterinarianName;
    private UUID clientId;
    private String clientName;
    private UUID animalId;
    private String animalName;
}
