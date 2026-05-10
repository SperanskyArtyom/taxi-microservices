package io.github.speranskyartyom.taxi_microservices.trip_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class PricingService {
    @Value("${app.pricing.fare-per-km}")
    private BigDecimal farePerKm;

    public BigDecimal calculatePrice(String origin, String destination) {
        double distance = calculateDistance(origin, destination);
        BigDecimal price = BigDecimal.valueOf(distance).multiply(farePerKm);

        log.debug("Price calculation: {} km * {} = {}",
                String.format("%.2f", distance), farePerKm, price);

        return price.setScale(2, RoundingMode.HALF_UP);
    }

    private double calculateDistance(String origin, String destination) {
        String[] start = origin.split(",");
        String[] end = destination.split(",");

        double lat1 = Double.parseDouble(start[0]);
        double lon1 = Double.parseDouble(start[1]);
        double lat2 = Double.parseDouble(end[0]);
        double lon2 = Double.parseDouble(end[1]);

        return calculateHaversine(lat1, lon1, lat2, lon2);
    }

    private double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Радиус Земли в км

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
