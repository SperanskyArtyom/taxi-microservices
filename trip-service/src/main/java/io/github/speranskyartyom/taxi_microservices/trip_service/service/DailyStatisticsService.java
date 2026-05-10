package io.github.speranskyartyom.taxi_microservices.trip_service.service;

import io.github.speranskyartyom.taxi_microservices.trip_service.dto.DailyStatisticsResponse;

import java.time.LocalDate;

public interface DailyStatisticsService {
    DailyStatisticsResponse getStatisticsForDay(LocalDate date);
}
