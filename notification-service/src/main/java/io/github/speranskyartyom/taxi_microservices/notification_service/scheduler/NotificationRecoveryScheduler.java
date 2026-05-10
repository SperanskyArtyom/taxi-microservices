package io.github.speranskyartyom.taxi_microservices.notification_service.scheduler;

import io.github.speranskyartyom.taxi_microservices.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRecoveryScheduler {
    private final NotificationRepository repository;

    @Scheduled(fixedDelay = 60 * 1000)
    public void recoverStaleTasks() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        int recoveredCount = repository.recoverStaleTasks(threshold);

        if (recoveredCount > 0) {
            log.warn("Recovered {} stale notification tasks", recoveredCount);
        }
    }
}
