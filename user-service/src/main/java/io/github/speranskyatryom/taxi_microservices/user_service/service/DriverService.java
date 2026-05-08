package io.github.speranskyatryom.taxi_microservices.user_service.service;

import io.github.speranskyatryom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.DriverResponse;

public interface DriverService {
    DriverResponse register(DriverRegistrationRequest request);
}
