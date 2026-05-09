package io.github.speranskyartyom.taxi_microservices.user_service.service;

import io.github.speranskyartyom.taxi_microservices.common.dto.PassengerResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.PassengerUpdateRequest;

import java.util.List;

public interface PassengerService {
    PassengerResponse register(PassengerRegistrationRequest request);

    PassengerResponse getById(Long id);

    List<PassengerResponse> getAll();

    PassengerResponse update(Long id, PassengerUpdateRequest request);

    void delete(Long id);
}
