package io.github.speranskyartyom.taxi_microservices.user_service.repository;

import io.github.speranskyartyom.taxi_microservices.user_service.domain.entity.Driver;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByPhone(String phone);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Driver d " +
            "WHERE d.isAvailable = true " +
            "ORDER BY d.id LIMIT 1")
    Optional<Driver> findFirstAvailable();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Driver d SET d.isAvailable = false WHERE d.id = :id")
    void markAsBusy(@Param("id") Long id);

    @Query("SELECT d.id FROM Driver d WHERE d.isAvailable = true")
    List<Long> findAllAvailableIds();
}
