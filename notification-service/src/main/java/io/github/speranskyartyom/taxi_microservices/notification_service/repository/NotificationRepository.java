package io.github.speranskyartyom.taxi_microservices.notification_service.repository;

import io.github.speranskyartyom.taxi_microservices.notification_service.domain.entity.NotificationTask;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationTask, Long> {
    List<NotificationTask> findAllByTripId(Long tripId);

    @Query(value = """
            UPDATE notification_tasks
            SET status = 'PROCESSING'
            WHERE id = (
                SELECT id
                FROM notification_tasks
                WHERE status IN ('PENDING', 'FAILED')
                ORDER BY created_at ASC
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            )
            RETURNING *
            """, nativeQuery = true)
    Optional<NotificationTask> findAndLockNextTask();

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE notification_tasks
            SET status = 'FAILED', 
                updated_at = CURRENT_TIMESTAMP,
                attempts = attempts + 1
            WHERE status = 'PROCESSING'
              AND updated_at < :timeout
            """, nativeQuery = true)
    int recoverStaleTasks(LocalDateTime timeout);
}
