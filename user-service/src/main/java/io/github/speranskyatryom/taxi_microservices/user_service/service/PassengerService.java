package io.github.speranskyatryom.taxi_microservices.user_service.service;

import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerResponse;

public interface PassengerService {
    PassengerResponse register(PassengerRegistrationRequest request);
}
