package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateMedicalAppointmentDto;
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
    date = "2025-12-04T21:26:31-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
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

    private String medicalAppointmentVeterinarianName(MedicalAppointment medicalAppointment) {
        Veterinarian veterinarian = medicalAppointment.getVeterinarian();
        if ( veterinarian == null ) {
            return null;
        }
        return veterinarian.getName();
    }

    private UUID medicalAppointmentVeterinarianId(MedicalAppointment medicalAppointment) {
        Veterinarian veterinarian = medicalAppointment.getVeterinarian();
        if ( veterinarian == null ) {
            return null;
        }
        return veterinarian.getId();
    }

    private String medicalAppointmentClientName(MedicalAppointment medicalAppointment) {
        Client client = medicalAppointment.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getName();
    }

    private UUID medicalAppointmentClientId(MedicalAppointment medicalAppointment) {
        Client client = medicalAppointment.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getId();
    }

    private String medicalAppointmentAnimalName(MedicalAppointment medicalAppointment) {
        Animal animal = medicalAppointment.getAnimal();
        if ( animal == null ) {
            return null;
        }
        return animal.getName();
    }

    private UUID medicalAppointmentAnimalId(MedicalAppointment medicalAppointment) {
        Animal animal = medicalAppointment.getAnimal();
        if ( animal == null ) {
            return null;
        }
        return animal.getId();
    }
}
