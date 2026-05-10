package io.github.speranskyartyom.taxi_microservices.user_service.service;

import io.github.speranskyartyom.taxi_microservices.common.dto.DriverResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.domain.entity.Driver;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.exception.AlreadyExistsException;
import io.github.speranskyartyom.taxi_microservices.common.exceptions.ResourceNotFoundException;
import io.github.speranskyartyom.taxi_microservices.user_service.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplementationTest {

    @Mock
    private DriverRepository repository;

    @InjectMocks
    private DriverServiceImplementation service;

    private Driver driver;
    private DriverRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = DriverRegistrationRequest.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("driver@taxi.io")
                .phone("+79001112233")
                .carInfo("Tesla Model 3, White")
                .build();

        driver = Driver.builder()
                .id(1L)
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("driver@taxi.io")
                .phone("+79001112233")
                .carInfo("Tesla Model 3, White")
                .isAvailable(false)
                .rating(BigDecimal.valueOf(5.0))
                .build();
    }

    @Test
    void register_ShouldSaveDriver_WhenRequestIsUnique() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByPhone(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Driver.class))).thenReturn(driver);

        DriverResponse response = service.register(registrationRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCarInfo()).isEqualTo(registrationRequest.getCarInfo());
        assertThat(response.getRating()).isEqualByComparingTo("5.0");
        verify(repository, times(1)).save(any(Driver.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(repository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.of(driver));

        assertThatThrownBy(() -> service.register(registrationRequest))
                .isInstanceOf(AlreadyExistsException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void updateStatus_ShouldToggleAvailability_WhenDriverExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(driver));

        service.updateStatus(1L, true);

        ArgumentCaptor<Driver> driverCaptor = ArgumentCaptor.forClass(Driver.class);
        verify(repository).save(driverCaptor.capture());

        Driver savedDriver = driverCaptor.getValue();
        assertThat(savedDriver.isAvailable()).isTrue();
        assertThat(savedDriver.getFirstName()).isEqualTo("Ivan");
    }

    @Test
    void updateStatus_ShouldNotSave_WhenNewStatusIsSameAsCurrent() {
        when(repository.findById(1L)).thenReturn(Optional.of(driver));

        service.updateStatus(1L, false);

        verify(repository).findById(1L);
        verify(repository, never()).save(any(Driver.class));
    }

    @Test
    void updateStatus_ShouldThrowException_WhenDriverNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(99L, true))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById_ShouldReturnDriverResponse_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(driver));

        DriverResponse response = service.getById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getLastName()).isEqualTo("Ivanov");
    }

    @Test
    void delete_ShouldInvokeRepository_WhenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}