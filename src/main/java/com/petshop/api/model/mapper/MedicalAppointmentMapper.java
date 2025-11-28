package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.request.UpdateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.model.entities.MedicalAppointment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentEndTime", ignore = true)
    @Mapping(target = "status", constant = "SCHEDULED")
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "animal", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    MedicalAppointment toEntity(CreateMedicalAppointmentDto createMedicalAppointmentDTO);

    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "veterinarianId", source = "veterinarian.id")
    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "animalName", source = "animal.name")
    @Mapping(target = "animalId", source = "animal.id")
    @Mapping(target = "appointmentStatus", source = "appointmentStatus")
    MedicalAppointmentResponseDto toResponseDto(MedicalAppointment medicalAppointment);
}





