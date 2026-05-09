package io.github.speranskyartyom.taxi_microservices.user_service.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "drivers")
@AttributeOverride(
        name = "lastName",
        column = @Column(name = "last_name", nullable = false)
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Driver extends User {

    @Column(name = "car_info", nullable = false)
    private String carInfo;

    @Builder.Default
    @Column(name = "is_available")
    private boolean isAvailable = false;
}
