package com.petshop.api.repository;

import com.petshop.api.model.entities.MedicalAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointment, UUID> {

    @Query("""
            SELECT EXISTS(
            SELECT 1 FROM MedicalAppointment a
            WHERE a.veterinarian.id = :veterinarianId
            AND :start < a.appointmentEndTime
            AND :end > a.appointmentStartTime
            )
            """)
    boolean existsConflictingAppointment(
            @Param("veterinarianId") UUID veterinarianId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    Page<MedicalAppointment> findByClientNameContainingIgnoreCase(String name, Pageable pageable);

    Page<MedicalAppointment> findByVeterinarianNameContainingIgnoreCase(String name, Pageable pageable);


    boolean existsByVeterinarianId(UUID veterinarianId);
}
