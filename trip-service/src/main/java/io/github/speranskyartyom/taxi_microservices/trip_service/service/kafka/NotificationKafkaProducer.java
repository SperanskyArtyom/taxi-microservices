package io.github.speranskyartyom.taxi_microservices.trip_service.service.kafka;

import io.github.speranskyartyom.taxi_microservices.common.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.notification}")
    private String topic;

    public void sendNotificationEvent(NotificationEvent event) {
        log.info("Sending notification event to Kafka for tripId: {}", event.tripId());

        kafkaTemplate.send(topic, String.valueOf(event.tripId()), event)
                .whenComplete((_, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic: {}", topic);
                    } else {
                        log.error("Failed to send message to Kafka", ex);
                    }
                });
    }
}
