package io.github.speranskyatryom.taxi_microservices.user_service.repository;

import io.github.speranskyatryom.taxi_microservices.user_service.domain.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByPhone(String phone);
}
