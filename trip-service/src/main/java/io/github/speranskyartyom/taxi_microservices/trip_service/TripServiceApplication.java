package io.github.speranskyartyom.taxi_microservices.trip_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class TripServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripServiceApplication.class, args);
    }

}
