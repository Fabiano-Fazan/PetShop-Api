package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
import com.petshop.api.dto.request.UpdateMedicalAppointmentDto;
import com.petshop.api.dto.response.MedicalAppointmentResponseDto;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.MedicalAppointment;
import com.petshop.api.model.entities.Veterinarian;
import com.petshop.api.model.enums.AppointmentStatus;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T21:00:22-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class MedicalAppointmentMapperImpl implements MedicalAppointmentMapper {

    @Override
    public MedicalAppointment toEntity(CreateMedicalAppointmentDto createMedicalAppointmentDTO) {
        if ( createMedicalAppointmentDTO == null ) {
            return null;
        }

        MedicalAppointment.MedicalAppointmentBuilder medicalAppointment = MedicalAppointment.builder();

        medicalAppointment.diagnosis( createMedicalAppointmentDTO.getDiagnosis() );
        medicalAppointment.treatment( createMedicalAppointmentDTO.getTreatment() );
        medicalAppointment.appointmentStartTime( createMedicalAppointmentDTO.getAppointmentStartTime() );
        medicalAppointment.durationMinutes( createMedicalAppointmentDTO.getDurationMinutes() );

        medicalAppointment.status( AppointmentStatus.SCHEDULED );

        return medicalAppointment.build();
    }

    @Override
    public MedicalAppointmentResponseDto toResponseDto(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }

        MedicalAppointmentResponseDto medicalAppointmentResponseDto = new MedicalAppointmentResponseDto();

        medicalAppointmentResponseDto.setVeterinarianName( medicalAppointmentVeterinarianName( medicalAppointment ) );
        medicalAppointmentResponseDto.setVeterinarianId( medicalAppointmentVeterinarianId( medicalAppointment ) );
        medicalAppointmentResponseDto.setClientName( medicalAppointmentClientName( medicalAppointment ) );
        medicalAppointmentResponseDto.setClientId( medicalAppointmentClientId( medicalAppointment ) );
        medicalAppointmentResponseDto.setAnimalName( medicalAppointmentAnimalName( medicalAppointment ) );
        medicalAppointmentResponseDto.setAnimalId( medicalAppointmentAnimalId( medicalAppointment ) );
        if ( medicalAppointment.getAppointmentStatus() != null ) {
            medicalAppointmentResponseDto.setAppointmentStatus( medicalAppointment.getAppointmentStatus().name() );
        }
        medicalAppointmentResponseDto.setId( medicalAppointment.getId() );
        medicalAppointmentResponseDto.setAppointmentStartTime( medicalAppointment.getAppointmentStartTime() );
        medicalAppointmentResponseDto.setAppointmentEndTime( medicalAppointment.getAppointmentEndTime() );
        medicalAppointmentResponseDto.setDiagnosis( medicalAppointment.getDiagnosis() );
        medicalAppointmentResponseDto.setTreatment( medicalAppointment.getTreatment() );

        return medicalAppointmentResponseDto;
    }

    @Override
    public void updateMedicalAppointmentDto(UpdateMedicalAppointmentDto updateMedicalAppointmentDto, MedicalAppointment medicalAppointment) {
        if ( updateMedicalAppointmentDto == null ) {
            return;
        }

        if ( updateMedicalAppointmentDto.getDiagnosis() != null ) {
            medicalAppointment.setDiagnosis( updateMedicalAppointmentDto.getDiagnosis() );
        }
        if ( updateMedicalAppointmentDto.getTreatment() != null ) {
            medicalAppointment.setTreatment( updateMedicalAppointmentDto.getTreatment() );
        }
        if ( updateMedicalAppointmentDto.getAppointmentStartTime() != null ) {
            medicalAppointment.setAppointmentStartTime( updateMedicalAppointmentDto.getAppointmentStartTime() );
        }
        if ( updateMedicalAppointmentDto.getDurationMinutes() != null ) {
            medicalAppointment.setDurationMinutes( updateMedicalAppointmentDto.getDurationMinutes() );
        }
    }

    private String medicalAppointmentVeterinarianName(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }
        Veterinarian veterinarian = medicalAppointment.getVeterinarian();
        if ( veterinarian == null ) {
            return null;
        }
        String name = veterinarian.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID medicalAppointmentVeterinarianId(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }
        Veterinarian veterinarian = medicalAppointment.getVeterinarian();
        if ( veterinarian == null ) {
            return null;
        }
        UUID id = veterinarian.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String medicalAppointmentClientName(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }
        Client client = medicalAppointment.getClient();
        if ( client == null ) {
            return null;
        }
        String name = client.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID medicalAppointmentClientId(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }
        Client client = medicalAppointment.getClient();
        if ( client == null ) {
            return null;
        }
        UUID id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String medicalAppointmentAnimalName(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }
        Animal animal = medicalAppointment.getAnimal();
        if ( animal == null ) {
            return null;
        }
        String name = animal.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID medicalAppointmentAnimalId(MedicalAppointment medicalAppointment) {
        if ( medicalAppointment == null ) {
            return null;
        }
        Animal animal = medicalAppointment.getAnimal();
        if ( animal == null ) {
            return null;
        }
        UUID id = animal.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
