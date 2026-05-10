package io.github.speranskyartyom.taxi_microservices.notification_service.service;

import io.github.speranskyartyom.taxi_microservices.notification_service.domain.constants.NotificationStatus;
import io.github.speranskyartyom.taxi_microservices.notification_service.domain.entity.NotificationTask;
import io.github.speranskyartyom.taxi_microservices.notification_service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationWorkerService {
    private final NotificationRepository repository;
    private final Executor taskExecutor;

    @Scheduled(fixedDelay = 500)
    public void fetchAndProcess() {
        repository.findAndLockNextTask().ifPresent(task ->
                taskExecutor.execute(() -> processTask(task)));
    }

    @Transactional
    private void processTask(NotificationTask task) {
        try {
            log.info("Processing notification {} for trip {}", task.getId(), task.getTripId());

            simulateSending(task);

            task = task.toBuilder().status(NotificationStatus.SENT).build();
            repository.save(task);
        } catch (Exception e) {
            log.error("Failed to send notification {}", task.getId(), e);
            handleFailure(task);
        }
    }

    private void handleFailure(NotificationTask task) {
        int attempts = task.getAttempts() + 1;

        task = task.toBuilder().attempts(attempts).build();

        if (attempts >= 3) {
            task = task.toBuilder().status(NotificationStatus.CANCELLED).build();
            log.warn("Task {} marked as CANCELLED after 3 attempts", task.getId());
        } else {
            task = task.toBuilder().status(NotificationStatus.FAILED).build();
        }

        repository.save(task);
    }

    private void simulateSending(NotificationTask task) throws InterruptedException {
        Thread.sleep(1000);
        if (Math.random() > 0.8) throw new RuntimeException("Network error");
    }
}
