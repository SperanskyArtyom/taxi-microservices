package io.github.speranskyartyom.taxi_microservices.trip_service.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        // FULL покажет и заголовки, и тело запроса/ответа
        return Logger.Level.FULL;
    }
}