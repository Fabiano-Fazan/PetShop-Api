package com.petshop.api.model.entities;

import com.petshop.api.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String notes;

    @Column(nullable = false)
    private String diagnosis;


    private String treatment;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Veterinarian veterinarian;

    @Column(nullable = false)
    private LocalDateTime appointmentStartTime;

    private LocalDateTime appointmentEndTime;

    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status",nullable = false)
    private AppointmentStatus appointmentStatus;

}
