package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.model.entities.MedicalAppointment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentEndTime", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "appointmentStatus", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    @Mapping(target = "notes", source = "notes")
    MedicalAppointment toEntity(CreateMedicalAppointmentDto dto);

    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "veterinarianId", source = "veterinarian.id")
    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "animalName", source = "animal.name")
    @Mapping(target = "animalId", source = "animal.id")
    @Mapping(target = "appointmentStatus", source = "appointmentStatus")
    @Mapping(target = "notes", source = "notes")
    MedicalAppointmentResponseDto toResponseDto(MedicalAppointment medicalAppointment);
}





