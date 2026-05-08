package io.github.speranskyatryom.taxi_microservices.user_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DriverResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String carInfo;
    private boolean isAvailable;
    private BigDecimal rating;
}
