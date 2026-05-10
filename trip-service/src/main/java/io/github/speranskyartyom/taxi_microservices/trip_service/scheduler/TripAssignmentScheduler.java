package io.github.speranskyartyom.taxi_microservices.trip_service.scheduler;

import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import io.github.speranskyartyom.taxi_microservices.trip_service.domain.entity.Trip;
import io.github.speranskyartyom.taxi_microservices.trip_service.repository.TripRepository;
import io.github.speranskyartyom.taxi_microservices.trip_service.service.TripServiceImplementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripAssignmentScheduler {
    private final TripRepository repository;
    private final TripServiceImplementation tripService;

    @Scheduled(fixedDelay = 10000)
    public void processPendingTrips() {
        List<Trip> pendingTrips = repository.findAllByStatus(TripStatus.CREATED);

        if (pendingTrips.isEmpty()) return;

        log.info("Found {} trips searching driver", pendingTrips.size());

        for (Trip trip : pendingTrips) {
            if (trip.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
                cancelTrip(trip);
                continue;
            }

            tripService.tryAssignDriver(trip);
        }
    }

    private void cancelTrip(Trip trip) {
        log.warn("The driver's wait time for order {} has expired. Cancel.", trip.getId());
        repository.save(trip.toBuilder().status(TripStatus.CANCELLED).build());
    }
}
