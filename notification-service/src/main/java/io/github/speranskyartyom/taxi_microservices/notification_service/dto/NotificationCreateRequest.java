package io.github.speranskyartyom.taxi_microservices.notification_service.dto;

import io.github.speranskyartyom.taxi_microservices.common.domain.entity.constants.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {
    @NotNull(message = "Trip id is required")
    private Long tripId;

    @NotNull(message = "Recipient type is required")
    private RecipientType recipientType;

    @NotNull(message = "Recipient id is required")
    private Long recipientId;

    @NotBlank(message = "Message is required")
    private String message;
}
