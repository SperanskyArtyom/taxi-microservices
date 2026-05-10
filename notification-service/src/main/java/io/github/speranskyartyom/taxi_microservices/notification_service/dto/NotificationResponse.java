package io.github.speranskyartyom.taxi_microservices.notification_service.dto;

import io.github.speranskyartyom.taxi_microservices.notification_service.domain.constants.NotificationStatus;
import io.github.speranskyartyom.taxi_microservices.common.domain.entity.constants.RecipientType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private Long tripId;
    private RecipientType recipientType;
    private Long recipientId;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
