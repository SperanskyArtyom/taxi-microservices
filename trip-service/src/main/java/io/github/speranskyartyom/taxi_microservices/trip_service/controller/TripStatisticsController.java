package io.github.speranskyartyom.taxi_microservices.trip_service.controller;

import io.github.speranskyartyom.taxi_microservices.trip_service.dto.DailyStatisticsResponse;
import io.github.speranskyartyom.taxi_microservices.trip_service.service.DailyStatisticsService;
import io.github.speranskyartyom.taxi_microservices.trip_service.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/trips/statistics")
@RequiredArgsConstructor
public class TripStatisticsController {
    private final DailyStatisticsService service;

    @GetMapping("/daily")
    public DailyStatisticsResponse getDailyStats(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getStatisticsForDay(date);
    }
}