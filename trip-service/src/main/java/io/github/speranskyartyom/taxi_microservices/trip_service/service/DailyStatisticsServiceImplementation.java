package io.github.speranskyartyom.taxi_microservices.trip_service.service;

import io.github.speranskyartyom.taxi_microservices.trip_service.dto.DailyStatisticsResponse;
import io.github.speranskyartyom.taxi_microservices.trip_service.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyStatisticsServiceImplementation implements DailyStatisticsService {
    private final TripRepository repository;

    @Override
    public DailyStatisticsResponse getStatisticsForDay(LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        List<Object[]> results = repository.getDailyStatistics(startOfDay, endOfDay);

        if (results.isEmpty() || results.getFirst()[0] == null || (long) results.getFirst()[0] == 0) {
            return DailyStatisticsResponse.builder()
                    .totalTrips(0)
                    .averagePrice(BigDecimal.ZERO)
                    .date(targetDate)
                    .build();
        }

        Object[] stats = results.getFirst();
        long count = (long) stats[0];

        BigDecimal avgPrice = BigDecimal.ZERO;
        if (stats[1] != null) {
            avgPrice = BigDecimal.valueOf((Double) stats[1]).setScale(2, RoundingMode.HALF_UP);
        }

        return DailyStatisticsResponse.builder()
                .totalTrips(count)
                .averagePrice(avgPrice)
                .date(targetDate)
                .build();
    }
}
