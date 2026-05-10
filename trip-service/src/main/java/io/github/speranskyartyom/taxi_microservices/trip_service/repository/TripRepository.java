package io.github.speranskyartyom.taxi_microservices.trip_service.repository;

import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import io.github.speranskyartyom.taxi_microservices.trip_service.domain.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByPassengerId(long passengerId);
    List<Trip> findAllByStatus(TripStatus status);
}
