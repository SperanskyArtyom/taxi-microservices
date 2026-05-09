package io.github.speranskyartyom.taxi_microservices.user_service.repository;

import io.github.speranskyartyom.taxi_microservices.user_service.domain.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhone(String phone);
}
