package io.github.speranskyartyom.taxi_microservices.notification_service.service;

import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationCreateRequest;
import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getNotificationsByTripId(Long tripId);

    NotificationResponse create(NotificationCreateRequest request);
}
