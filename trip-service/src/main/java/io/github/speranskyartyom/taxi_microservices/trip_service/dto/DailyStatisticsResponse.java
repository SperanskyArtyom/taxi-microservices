package io.github.speranskyartyom.taxi_microservices.trip_service.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record DailyStatisticsResponse(
        long totalTrips,
        BigDecimal averagePrice,
        LocalDate date
) {}
