package io.github.speranskyartyom.taxi_microservices.trip_service.service;

import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import io.github.speranskyartyom.taxi_microservices.trip_service.dto.TripCreateRequest;
import io.github.speranskyartyom.taxi_microservices.trip_service.dto.TripResponse;

import java.util.List;

public interface TripService {
    TripResponse create(TripCreateRequest request);
    TripResponse getById(Long id);
    List<TripResponse> getAllByPassengerId(Long passengerId);
    void updateStatus(Long id, TripStatus status);
    void rateById(Long id, Integer rating);
}
