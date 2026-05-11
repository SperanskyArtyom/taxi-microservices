package io.github.speranskyartyom.taxi_microservices.user_service.controller;

import io.github.speranskyartyom.taxi_microservices.common.dto.DriverIdResponse;
import io.github.speranskyartyom.taxi_microservices.common.dto.DriverResponse;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.dto.DriverUpdateRequest;
import io.github.speranskyartyom.taxi_microservices.user_service.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public DriverResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DriverResponse> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public DriverResponse update(@PathVariable Long id, @Valid @RequestBody DriverUpdateRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void changeStatus(@PathVariable Long id, @RequestParam boolean isAvailable) {
        service.updateStatus(id, isAvailable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/assign")
    public DriverIdResponse assign() {
        return DriverIdResponse.builder()
                .id(service.assignAvailableDriver())
                .build();
    }
}
