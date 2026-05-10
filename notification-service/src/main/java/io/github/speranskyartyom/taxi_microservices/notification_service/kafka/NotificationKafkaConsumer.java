package io.github.speranskyartyom.taxi_microservices.notification_service.kafka;

import io.github.speranskyartyom.taxi_microservices.common.dto.NotificationEvent;
import io.github.speranskyartyom.taxi_microservices.notification_service.domain.constants.NotificationStatus;
import io.github.speranskyartyom.taxi_microservices.notification_service.dto.NotificationCreateRequest;
import io.github.speranskyartyom.taxi_microservices.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumer {
    private final NotificationService service;

    @KafkaListener(topics = "${app.kafka.topics.notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(NotificationEvent event) {
        log.info("Received notification event for tripId: {}", event.tripId());

        NotificationCreateRequest request = NotificationCreateRequest.builder()
                .tripId(event.tripId())
                .recipientType(event.recipientType())
                .recipientId(event.recipientId())
                .message(event.message())
                .build();

        service.create(request);
    }
}
