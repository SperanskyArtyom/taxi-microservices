package io.github.speranskyartyom.taxi_microservices.trip_service.client;

import io.github.speranskyartyom.taxi_microservices.common.dto.DriverIdResponse;
import io.github.speranskyartyom.taxi_microservices.common.dto.PassengerResponse;
import io.github.speranskyartyom.taxi_microservices.trip_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", url = "${user-service.url}", configuration = FeignConfig.class)
public interface UserServiceClient {
    @GetMapping("/api/v1/passengers/{id}")
    PassengerResponse getPassenger(@PathVariable Long id);

    @PostMapping("/api/v1/drivers/assign")
    DriverIdResponse assignDriver();
}
