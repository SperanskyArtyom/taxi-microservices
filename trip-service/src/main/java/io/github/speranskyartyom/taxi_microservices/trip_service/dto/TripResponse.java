package io.github.speranskyartyom.taxi_microservices.trip_service.dto;

import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TripResponse {
    private Long id;
    private Long passengerId;
    private Long driverId;
    private TripStatus status;
    private String origin;
    private String destination;
    private BigDecimal price;
    private Integer rating;
}
