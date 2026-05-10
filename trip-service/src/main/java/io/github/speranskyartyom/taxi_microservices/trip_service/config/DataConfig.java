package io.github.speranskyartyom.taxi_microservices.trip_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.github.speranskyartyom.taxi_microservices.trip_service.repository")
public class DataConfig {
}
