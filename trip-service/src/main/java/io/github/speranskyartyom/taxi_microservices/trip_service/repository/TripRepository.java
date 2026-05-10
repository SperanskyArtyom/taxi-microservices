package io.github.speranskyartyom.taxi_microservices.trip_service.repository;

import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import io.github.speranskyartyom.taxi_microservices.trip_service.domain.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByPassengerId(long passengerId);
    List<Trip> findAllByStatus(TripStatus status);
    @Query("""
        SELECT COUNT(t), AVG(t.price) 
        FROM Trip t 
        WHERE t.createdAt >= :startOfDay AND t.createdAt <= :endOfDay
    """)
    List<Object[]> getDailyStatistics(@Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay);
}
