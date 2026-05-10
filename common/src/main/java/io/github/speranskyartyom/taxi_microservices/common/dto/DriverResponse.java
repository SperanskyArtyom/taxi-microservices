package io.github.speranskyartyom.taxi_microservices.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
