package io.github.speranskyartyom.taxi_microservices.trip_service.controller;

import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import io.github.speranskyartyom.taxi_microservices.trip_service.dto.TripCreateRequest;
import io.github.speranskyartyom.taxi_microservices.trip_service.dto.TripResponse;
import io.github.speranskyartyom.taxi_microservices.trip_service.service.TripService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponse createTrip(@Valid @RequestBody TripCreateRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public TripResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<TripResponse> getAllByPassengerId(@RequestParam Long passengerId) {
        return service.getAllByPassengerId(passengerId);
    }

    @PatchMapping("/{id}/status")
    public void changeStatus(@PathVariable Long id, @RequestParam TripStatus status) {
        service.updateStatus(id, status);
    }

    @PatchMapping("/{id}/rating")
    public void rateById(@PathVariable Long id, @RequestParam @Min(1) @Max(5) Integer rating) {
        service.rateById(id, rating);
    }
}
