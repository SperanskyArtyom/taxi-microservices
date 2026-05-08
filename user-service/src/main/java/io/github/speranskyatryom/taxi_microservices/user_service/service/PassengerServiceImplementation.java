package io.github.speranskyatryom.taxi_microservices.user_service.service;

import io.github.speranskyatryom.taxi_microservices.user_service.domain.entity.Passenger;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerResponse;
import io.github.speranskyatryom.taxi_microservices.user_service.exception.AlreadyExistsException;
import io.github.speranskyatryom.taxi_microservices.user_service.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerServiceImplementation implements PassengerService {
    private final PassengerRepository repository;

    @Override
    @Transactional
    public PassengerResponse register(PassengerRegistrationRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistsException("Passenger with this email already exists");
        }
        if (repository.findByPhone(request.getPhone()).isPresent()) {
            throw new AlreadyExistsException("Passenger with this phone already exists");
        }

        Passenger passenger = mapToEntity(request);

        Passenger savedPassenger = repository.save(passenger);

        return mapToResponse(savedPassenger);
    }

    private Passenger mapToEntity(PassengerRegistrationRequest request) {
        return Passenger.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

    private PassengerResponse mapToResponse(Passenger passenger) {
        return PassengerResponse.builder()
                .id(passenger.getId())
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .email(passenger.getEmail())
                .phone(passenger.getPhone())
                .rating(passenger.getRating())
                .build();
    }
}
