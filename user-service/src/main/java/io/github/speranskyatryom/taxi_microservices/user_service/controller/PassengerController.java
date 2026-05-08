package io.github.speranskyatryom.taxi_microservices.user_service.controller;

import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerResponse;
import io.github.speranskyatryom.taxi_microservices.user_service.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponse register(@Valid @RequestBody PassengerRegistrationRequest request) {
        return service.register(request);
    }
}
