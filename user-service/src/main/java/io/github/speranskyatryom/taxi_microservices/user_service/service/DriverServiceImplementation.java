package io.github.speranskyatryom.taxi_microservices.user_service.service;

import io.github.speranskyatryom.taxi_microservices.user_service.domain.entity.Driver;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.DriverResponse;
import io.github.speranskyatryom.taxi_microservices.user_service.exception.AlreadyExistsException;
import io.github.speranskyatryom.taxi_microservices.user_service.repository.DriverRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImplementation implements DriverService {
    private final DriverRepository repository;

    @Override
    @Transactional
    public DriverResponse register(DriverRegistrationRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistsException("Driver with this email already exists");
        }
        if (repository.findByPhone(request.getPhone()).isPresent()) {
            throw new AlreadyExistsException("Driver with this phone already exists");
        }

        Driver driver = mapToEntity(request);

        Driver savedDriver = repository.save(driver);

        return mapToResponse(savedDriver);
    }

    private Driver mapToEntity(DriverRegistrationRequest request) {
        return Driver.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .carInfo(request.getCarInfo())
                .build();
    }

    private DriverResponse mapToResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .carInfo(driver.getCarInfo())
                .isAvailable(driver.isAvailable())
                .rating(driver.getRating())
                .build();
    }
}
