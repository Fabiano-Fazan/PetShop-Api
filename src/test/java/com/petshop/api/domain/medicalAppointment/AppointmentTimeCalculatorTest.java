package com.petshop.api.domain.medicalAppointment;

import com.petshop.api.exception.AppointmentDateTimeAlreadyExistsException;
import com.petshop.api.repository.MedicalAppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentTimeCalculatorTest {

    @InjectMocks
    private AppointmentTimeCalculator appointmentTimeCalculator;

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;


    @Test
    @DisplayName("Should return input duration when it is not null")
    void duration_ShouldReturnInput_WhenNotNull() {

        Integer input = 60;
        int defaultValue = 30;

        int result = appointmentTimeCalculator.duration(input, defaultValue);

        assertThat(result).isEqualTo(60);
    }


    @Test
    @DisplayName("Should return default duration when input is null")
    void duration_ShouldReturnDefault_WhenInputIsNull() {

        Integer input = null;
        int defaultValue = 45;

        int result = appointmentTimeCalculator.duration(input, defaultValue);

        assertThat(result).isEqualTo(45);
    }


    @Test
    @DisplayName("Should return input start time when it is not null")
    void start_ShouldReturnInput_WhenNotNull() {

        LocalDateTime inputStart = LocalDateTime.now().plusDays(1);
        LocalDateTime defaultStart = LocalDateTime.now();

        LocalDateTime result = appointmentTimeCalculator.start(inputStart, defaultStart);

        assertThat(result).isEqualTo(inputStart);
    }


    @Test
    @DisplayName("Should return default start time when input is null")
    void start_ShouldReturnDefault_WhenInputIsNull() {

        LocalDateTime inputStart = null;
        LocalDateTime defaultStart = LocalDateTime.now();

        LocalDateTime result = appointmentTimeCalculator.start(inputStart, defaultStart);

        assertThat(result).isEqualTo(defaultStart);
    }


    @Test
    @DisplayName("Should calculate end time based on input duration")
    void end_ShouldCalculateWithInputDuration_WhenNotNull() {

        LocalDateTime start = LocalDateTime.of(2026, 1, 19, 10, 0);
        Integer inputDuration = 60;

        LocalDateTime result = appointmentTimeCalculator.end(start, inputDuration);

        LocalDateTime expectedEnd = start.plusMinutes(60);
        assertThat(result).isEqualTo(expectedEnd);
    }

    @Test
    @DisplayName("Should calculate end time based on static default constant (30 min) when duration is null")
    void end_ShouldCalculateWithDefaultConstant_WhenInputDurationIsNull() {

        LocalDateTime start = LocalDateTime.of(2026, 1, 19, 10, 0);
        Integer inputDuration = null;

        LocalDateTime result = appointmentTimeCalculator.end(start, inputDuration);

        LocalDateTime expectedEnd = start.plusMinutes(30);
        assertThat(result).isEqualTo(expectedEnd);
    }


    @Test
    @DisplayName("Should throw exception when conflict exists")
    void validate_ShouldThrowException_WhenConflictFound() {

        UUID vetId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(30);

        when(medicalAppointmentRepository.existsConflictingAppointment(vetId, start, end)).thenReturn(true);

        assertThatThrownBy(() -> appointmentTimeCalculator.validateAppointmentTimeConflict(vetId, start, end))
                .isInstanceOf(AppointmentDateTimeAlreadyExistsException.class)
                .hasMessage("This time slot is already booked for this veterinarian");

        verify(medicalAppointmentRepository).existsConflictingAppointment(vetId, start, end);
    }

    @Test
    @DisplayName("Should do nothing when no conflict exists")
    void validate_ShouldPass_WhenNoConflict() {

        UUID vetId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(30);

        when(medicalAppointmentRepository.existsConflictingAppointment(vetId, start, end)).thenReturn(false);

        assertThatCode(() -> appointmentTimeCalculator.validateAppointmentTimeConflict(vetId, start, end)).doesNotThrowAnyException();

        verify(medicalAppointmentRepository).existsConflictingAppointment(vetId, start, end);
    }
}