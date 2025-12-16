package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.request.UpdateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.service.MedicalAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medical-appointments")
@RequiredArgsConstructor
public class MedicalAppointmentController {
    private final MedicalAppointmentService medicalAppointmentService;

    @GetMapping
    public ResponseEntity<Page<MedicalAppointmentResponseDto>> getAllMedicalAppointments(Pageable pageable){
        Page<MedicalAppointmentResponseDto> allMedicalAppointments = medicalAppointmentService.getAllMedicalAppointments(pageable);
        return ResponseEntity.ok(allMedicalAppointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalAppointmentResponseDto> getMedicalAppointmentById(@PathVariable UUID id){
        MedicalAppointmentResponseDto medicalAppointmentById = medicalAppointmentService.getMedicalAppointmentById(id);
        return  ResponseEntity.ok(medicalAppointmentById);
    }

    @GetMapping("/veterinarian")
    public ResponseEntity<Page<MedicalAppointmentResponseDto>> getMedicalAppointmentsByVeterinarianName(@RequestParam String name, Pageable pageable){
        Page<MedicalAppointmentResponseDto> medicalAppointments = medicalAppointmentService.getMedicalAppointmentsByVeterinarianNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(medicalAppointments);
    }

    @GetMapping("/client")
    public ResponseEntity<Page<MedicalAppointmentResponseDto>> getMedicalAppointmentsByClientName(@RequestParam String name, Pageable pageable){
        Page<MedicalAppointmentResponseDto> medicalAppointments = medicalAppointmentService.getMedicalAppointmentsByClientNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(medicalAppointments);
    }

    @PostMapping
    public ResponseEntity<MedicalAppointmentResponseDto> createMedicalAppointment(@Valid @RequestBody CreateMedicalAppointmentDto createMedicalAppointmentDto){
        MedicalAppointmentResponseDto createdMedicalAppointment = medicalAppointmentService.createMedicalAppointment(createMedicalAppointmentDto);
        return new ResponseEntity<>(createdMedicalAppointment, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MedicalAppointmentResponseDto> updateMedicalAppointment(@PathVariable UUID id, @Valid @RequestBody UpdateMedicalAppointmentDto updateMedicalAppointmentDto){
        MedicalAppointmentResponseDto updatedMedicalAppointment = medicalAppointmentService.updateMedicalAppointment(id, updateMedicalAppointmentDto);
        return ResponseEntity.ok(updatedMedicalAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalAppointment(@PathVariable UUID id){
        medicalAppointmentService.deleteMedicalAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
