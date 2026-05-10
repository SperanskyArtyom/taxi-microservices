package io.github.speranskyartyom.taxi_microservices.user_service.service;

import io.github.speranskyartyom.taxi_microservices.common.dto.PassengerResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.domain.entity.Passenger;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.exception.AlreadyExistsException;
import io.github.speranskyartyom.taxi_microservices.common.exceptions.ResourceNotFoundException;
import io.github.speranskyartyom.taxi_microservices.user_service.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplementationTest {

    @Mock
    private PassengerRepository repository;

    @InjectMocks
    private PassengerServiceImplementation service;

    private PassengerRegistrationRequest registrationRequest;
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        registrationRequest = PassengerRegistrationRequest.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@example.com")
                .phone("+79991234567")
                .build();

        passenger = Passenger.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@example.com")
                .phone("+79991234567")
                .build();
    }

    @Test
    void register_ShouldSavePassenger_WhenDataIsValid() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByPhone(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Passenger.class))).thenReturn(passenger);

        PassengerResponse response = service.register(registrationRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(registrationRequest.getEmail());
        verify(repository).save(any(Passenger.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        when(repository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.of(passenger));

        assertThatThrownBy(() -> service.register(registrationRequest))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("already exists");

        verify(repository, never()).save(any());
    }

    @Test
    void getById_ShouldReturnResponse_WhenPassengerExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(passenger));

        PassengerResponse response = service.getById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("Ivan");
    }

    @Test
    void getById_ShouldThrowException_WhenPassengerNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}