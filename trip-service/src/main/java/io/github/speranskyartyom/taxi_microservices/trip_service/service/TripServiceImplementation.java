package io.github.speranskyartyom.taxi_microservices.trip_service.service;

import feign.FeignException;
import io.github.speranskyartyom.taxi_microservices.common.domain.entity.constants.RecipientType;
import io.github.speranskyartyom.taxi_microservices.common.dto.NotificationEvent;
import io.github.speranskyartyom.taxi_microservices.common.exceptions.ResourceNotFoundException;
import io.github.speranskyartyom.taxi_microservices.trip_service.client.UserServiceClient;
import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import io.github.speranskyartyom.taxi_microservices.trip_service.domain.entity.Trip;
import io.github.speranskyartyom.taxi_microservices.trip_service.dto.TripCreateRequest;
import io.github.speranskyartyom.taxi_microservices.trip_service.dto.TripResponse;
import io.github.speranskyartyom.taxi_microservices.trip_service.repository.TripRepository;
import io.github.speranskyartyom.taxi_microservices.trip_service.service.kafka.NotificationKafkaProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripServiceImplementation implements TripService {
    private static final BigDecimal BASE_FARE = BigDecimal.valueOf(100.0);

    private final TripRepository repository;
    private final UserServiceClient userServiceClient;
    private final NotificationKafkaProducer notificationKafkaProducer;

    @Override
    @Transactional
    public TripResponse create(TripCreateRequest request) {
        userServiceClient.getPassenger(request.getPassengerId());

        BigDecimal price = calculatePrice(request.getOrigin(), request.getDestination());
        Long driverId = null;
        TripStatus status = TripStatus.CREATED;

        try {
            driverId = userServiceClient.assignDriver().getId();
            status = TripStatus.ACCEPTED;
        } catch (FeignException ex) {
            if (ex.status() == HttpStatus.CONFLICT.value()) {
                log.info(
                        "No drivers found for passenger {}, trip placed on hold",
                        request.getPassengerId()
                );
            } else {
                log.error("Unexpected feign error: {}", ex.getMessage());
            }
        }

        Trip trip = Trip.builder()
                .passengerId(request.getPassengerId())
                .driverId(driverId)
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(status)
                .price(price)
                .build();

        Trip savedTrip = repository.save(trip);

        NotificationEvent event;
        if (driverId == null) {
            event = NotificationEvent.builder()
                    .tripId(savedTrip.getId())
                    .recipientType(RecipientType.PASSENGER)
                    .recipientId(savedTrip.getPassengerId())
                    .message("Ваш заказ принят! Ищем водителя.")
                    .build();

        } else {
            event = NotificationEvent.builder()
                    .tripId(savedTrip.getId())
                    .recipientType(RecipientType.PASSENGER)
                    .recipientId(savedTrip.getPassengerId())
                    .message("Ваш заказ принят! Водитель уже спешит к вам!")
                    .build();

            notificationKafkaProducer.sendNotificationEvent(event);

            event = NotificationEvent.builder()
                    .tripId(savedTrip.getId())
                    .recipientType(RecipientType.DRIVER)
                    .recipientId(driverId)
                    .message("Вам назначен заказ! Пассажир ждёт вас по адресу " + savedTrip.getOrigin() + ".")
                    .build();

        }
        notificationKafkaProducer.sendNotificationEvent(event);
        return mapToResponse(savedTrip);
    }

    @Transactional
    public void tryAssignDriver(Trip trip) {
        try {
            Long driverId = userServiceClient.assignDriver().getId();
            Trip updatedTrip = trip.toBuilder()
                    .driverId(driverId)
                    .status(TripStatus.ACCEPTED)
                    .build();
            repository.save(updatedTrip);

            NotificationEvent event = NotificationEvent.builder()
                    .tripId(updatedTrip.getId())
                    .recipientType(RecipientType.PASSENGER)
                    .recipientId(updatedTrip.getPassengerId())
                    .message("На ваш заказ назначен водитель!")
                    .build();

            notificationKafkaProducer.sendNotificationEvent(event);

            event = NotificationEvent.builder()
                    .tripId(updatedTrip.getId())
                    .recipientType(RecipientType.DRIVER)
                    .recipientId(driverId)
                    .message("Вам назначен заказ! Пассажир ждёт вас по адресу " + updatedTrip.getOrigin() + ".")
                    .build();

            notificationKafkaProducer.sendNotificationEvent(event);
            log.info("Driver {} successfully assigned for trip {}", driverId, trip.getId());
        } catch (FeignException.Conflict _) {
        }
    }

    @Override
    public TripResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip with id: " + id + " not found")
                );
    }

    @Override
    public List<TripResponse> getAllByPassengerId(Long passengerId) {
        return repository.findAllByPassengerId(passengerId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void updateStatus(Long id, TripStatus status) {
        Trip trip = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip with id: " + id + " not found")
                );

        if (status != trip.getStatus()) {
            Trip updated = trip.toBuilder().status(status).build();
            repository.save(updated);

            if (status == TripStatus.FINISHED) userServiceClient.markAsAvailable(trip.getDriverId(), true);
            else if (status == TripStatus.CANCELLED && trip.getDriverId() != null) {
                userServiceClient.markAsAvailable(trip.getDriverId(), true);
            }

            NotificationEvent event = NotificationEvent.builder()
                    .tripId(updated.getId())
                    .recipientType(RecipientType.PASSENGER)
                    .recipientId(updated.getPassengerId())
                    .message("Статус вашей поездки изменился на " + status)
                    .build();

            notificationKafkaProducer.sendNotificationEvent(event);

            event = NotificationEvent.builder()
                    .tripId(updated.getId())
                    .recipientType(RecipientType.DRIVER)
                    .recipientId(updated.getDriverId())
                    .message("Статус вашей поездки изменился на " + status)
                    .build();

            notificationKafkaProducer.sendNotificationEvent(event);
        }
    }

    @Override
    public void rateById(Long id, Integer rating) {
        Trip trip = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip with id: " + id + " not found")
                );

        Trip rated = trip.toBuilder().rating(rating).build();
        repository.save(rated);

        NotificationEvent event = NotificationEvent.builder()
                .tripId(rated.getId())
                .recipientType(RecipientType.DRIVER)
                .recipientId(rated.getDriverId())
                .message("Вашей поездке поставили оценку " + rating)
                .build();

        notificationKafkaProducer.sendNotificationEvent(event);
    }

    private BigDecimal calculatePrice(String from, String to) {
        //TODO: реализовать правильный расчёт цены
        return BASE_FARE.add(BigDecimal.valueOf((from.length() + to.length()) * 5.0));
    }

    private TripResponse mapToResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .passengerId(trip.getPassengerId())
                .driverId(trip.getDriverId())
                .status(trip.getStatus())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .price(trip.getPrice())
                .rating(trip.getRating())
                .build();
    }
}
