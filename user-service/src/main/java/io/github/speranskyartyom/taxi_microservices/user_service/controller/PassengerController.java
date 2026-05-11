package io.github.speranskyartyom.taxi_microservices.user_service.controller;

import io.github.speranskyartyom.taxi_microservices.common.dto.PassengerResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.PassengerUpdateRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public PassengerResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PassengerResponse> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public PassengerResponse update(@PathVariable Long id, @Valid @RequestBody PassengerUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
