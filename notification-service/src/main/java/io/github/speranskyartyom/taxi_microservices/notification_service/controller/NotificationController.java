package io.github.speranskyartyom.taxi_microservices.notification_service.controller;

import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationCreateRequest;
import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationResponse;
import io.github.speranskyartyom.taxi_microservices.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    public List<NotificationResponse> getByTripId(@RequestParam Long tripId) {
        return service.getNotificationsByTripId(tripId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(@Valid @RequestBody NotificationCreateRequest request) {
        return service.create(request);
    }
}
