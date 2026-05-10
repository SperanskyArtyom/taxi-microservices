package io.github.speranskyartyom.taxi_microservices.trip_service.domain.entity;

import io.github.speranskyartyom.taxi_microservices.common.domain.entity.BaseEntity;
import io.github.speranskyartyom.taxi_microservices.trip_service.domain.constant.TripStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "trips")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Trip extends BaseEntity {
    @Column(name = "passenger_id", nullable = false)
    private Long passengerId;

    @Column(name = "driver_id")
    private Long driverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    private BigDecimal price;

    private Integer rating;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
