package io.github.speranskyartyom.taxi_microservices.common.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PassengerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private BigDecimal rating;
}
