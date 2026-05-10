package io.github.speranskyartyom.taxi_microservices.trip_service.dto;

import io.github.speranskyartyom.taxi_microservices.trip_service.validation.IsLocation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripCreateRequest {
    @NotNull(message = "Passenger id is required")
    private Long passengerId;

    @NotBlank(message = "Origin is required")
    @Size(max = 255, message = "Origin must be less than 255 characters")
    @IsLocation
    private String origin;

    @NotBlank(message = "Destination is required")
    @Size(max = 255, message = "Destination must be less than 255 characters")
    @IsLocation
    private String destination;
}
