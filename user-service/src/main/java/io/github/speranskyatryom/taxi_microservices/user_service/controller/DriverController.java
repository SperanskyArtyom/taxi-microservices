package io.github.speranskyatryom.taxi_microservices.user_service.controller;

import io.github.speranskyatryom.taxi_microservices.user_service.dto.*;
import io.github.speranskyatryom.taxi_microservices.user_service.service.DriverService;
import io.github.speranskyartyom.taxi_microservices.common.dto.DriverResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public DriverResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<DriverResponse> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{id}")
    public DriverResponse update(@PathVariable Long id, @Valid @RequestBody DriverUpdateRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public void changeStatus(@PathVariable Long id, @RequestParam boolean isAvailable) {
        service.updateStatus(id, isAvailable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
