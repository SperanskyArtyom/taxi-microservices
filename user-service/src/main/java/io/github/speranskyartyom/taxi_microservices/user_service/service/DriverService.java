package io.github.speranskyartyom.taxi_microservices.user_service.service;

import io.github.speranskyartyom.taxi_microservices.common.dto.DriverResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverUpdateRequest;

import java.util.List;

public interface DriverService {
    DriverResponse register(DriverRegistrationRequest request);

    DriverResponse getById(Long id);

    List<DriverResponse> getAll();

    DriverResponse update(Long id, DriverUpdateRequest request);

    void updateStatus(Long id, boolean isAvailable);

    void delete(Long id);
}
