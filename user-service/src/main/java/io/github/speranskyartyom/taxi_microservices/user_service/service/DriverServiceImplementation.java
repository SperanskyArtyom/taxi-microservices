package io.github.speranskyartyom.taxi_microservices.user_service.service;

import io.github.speranskyartyom.taxi_microservices.common.dto.DriverResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.domain.entity.Driver;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverUpdateRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.exception.AlreadyExistsException;
import io.github.speranskyartyom.taxi_microservices.user_service.exception.ResourceNotFoundException;
import io.github.speranskyartyom.taxi_microservices.user_service.repository.DriverRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public DriverResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver with id: " + id + " not found")
                );
    }

    @Override
    public List<DriverResponse> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public DriverResponse update(Long id, DriverUpdateRequest request) {
        Driver driver = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver with id: " + id + " not found")
                );

        Driver.DriverBuilder builder = driver.toBuilder();
        if (request.getFirstName() != null) builder.firstName(request.getFirstName());
        if (request.getLastName() != null) builder.lastName(request.getLastName());
        if (request.getEmail() != null) builder.email(request.getEmail());
        if (request.getPhone() != null) builder.phone(request.getPhone());
        if (request.getCarInfo() != null) builder.carInfo(request.getCarInfo());

        Driver updated = builder.build();
        return mapToResponse(repository.save(updated));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, boolean isAvailable) {
        Driver driver = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver with id: " + id + " not found")
                );

        if (isAvailable != driver.isAvailable()) {
            Driver updated = driver.toBuilder().isAvailable(isAvailable).build();
            repository.save(updated);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Driver with id: " + id + " not found");
        }
        repository.deleteById(id);
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
