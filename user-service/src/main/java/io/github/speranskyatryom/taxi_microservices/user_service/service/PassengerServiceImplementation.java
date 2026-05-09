package io.github.speranskyatryom.taxi_microservices.user_service.service;

import io.github.speranskyatryom.taxi_microservices.user_service.domain.entity.Passenger;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerRegistrationRequest;
import io.github.speranskyartyom.taxi_microservices.common.dto.PassengerResponse;
import io.github.speranskyatryom.taxi_microservices.user_service.dto.PassengerUpdateRequest;
import io.github.speranskyatryom.taxi_microservices.user_service.exception.AlreadyExistsException;
import io.github.speranskyatryom.taxi_microservices.user_service.exception.ResourceNotFoundException;
import io.github.speranskyatryom.taxi_microservices.user_service.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PassengerResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Passenger with id: " + id + " not found")
                );
    }

    @Override
    public List<PassengerResponse> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public PassengerResponse update(Long id, PassengerUpdateRequest request) {
        Passenger passenger = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Passenger with id: " + id + " not found")
                );

        Passenger.PassengerBuilder builder = passenger.toBuilder();
        if (request.getFirstName() != null) builder.firstName(request.getFirstName());
        if (request.getLastName() != null) builder.lastName(request.getLastName());
        if (request.getEmail() != null) builder.email(request.getEmail());
        if (request.getPhone() != null) builder.phone(request.getPhone());

        Passenger updated = builder.build();
        return mapToResponse(repository.save(updated));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Passenger with id: " + id + " not found");
        }
        repository.deleteById(id);
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
