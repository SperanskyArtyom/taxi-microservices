package io.github.speranskyartyom.taxi_microservices.notification_service.service;

import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationCreateRequest;
import io.github.speranskyartyom.taxi_microservices.notification_service.domain.entity.NotificationTask;
import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationResponse;
import io.github.speranskyartyom.taxi_microservices.notification_service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationService {
    private final NotificationRepository repository;

    @Override
    public List<NotificationResponse> getNotificationsByTripId(Long tripId) {
        return repository.findAllByTripId(tripId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public NotificationResponse create(NotificationCreateRequest request) {
        NotificationTask task = mapToEntity(request);
        NotificationTask saved = repository.save(task);
        return mapToResponse(saved);
    }

    private NotificationTask mapToEntity(NotificationCreateRequest request) {
        return NotificationTask.builder()
                .tripId(request.getTripId())
                .recipientType(request.getRecipientType())
                .recipientId(request.getRecipientId())
                .message(request.getMessage())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private NotificationResponse mapToResponse(NotificationTask task) {
        return NotificationResponse.builder()
                .id(task.getId())
                .tripId(task.getTripId())
                .recipientType(task.getRecipientType())
                .recipientId(task.getRecipientId())
                .message(task.getMessage())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
