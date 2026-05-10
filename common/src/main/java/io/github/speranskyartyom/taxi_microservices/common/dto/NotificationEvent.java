package io.github.speranskyartyom.taxi_microservices.common.dto;

import io.github.speranskyartyom.taxi_microservices.common.domain.entity.constants.RecipientType;
import lombok.Builder;

@Builder
public record NotificationEvent(
        Long tripId,
        RecipientType recipientType,
        Long recipientId,
        String message
) {
}
