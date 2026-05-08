package io.github.speranskyatryom.taxi_microservices.user_service.controller;

import io.github.speranskyatryom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.DriverResponse;
import io.github.speranskyatryom.taxi_microservices.user_service.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse register(@Valid @RequestBody DriverRegistrationRequest request) {
        return service.register(request);
    }
}
