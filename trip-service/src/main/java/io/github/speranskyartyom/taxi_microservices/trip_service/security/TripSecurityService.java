package io.github.speranskyartyom.taxi_microservices.trip_service.security;

import io.github.speranskyartyom.taxi_microservices.common.security.UserPrincipal;
import io.github.speranskyartyom.taxi_microservices.trip_service.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("tripSecurity")
@RequiredArgsConstructor
public class TripSecurityService {
    private final TripRepository repository;

    public boolean isTripParticipant(Long tripId, UserPrincipal principal) {
        if (principal == null) return false;

        return repository.findById(tripId)
                .map(trip -> trip.getPassengerId().equals(principal.getId()) ||
                        trip.getDriverId().equals(principal.getId()))
                .orElse(false);
    }

    public boolean isTripPassenger(Long tripId, UserPrincipal principal) {
        if (principal == null) return false;

        return repository.findById(tripId)
                .map(trip -> trip.getPassengerId().equals(principal.getId()))
                .orElse(false);
    }
}
