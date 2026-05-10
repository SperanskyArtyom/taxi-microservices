package io.github.speranskyartyom.taxi_microservices.user_service.redis;

import io.github.speranskyartyom.taxi_microservices.user_service.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheWarmUpRunner implements ApplicationRunner {
    private final DriverRepository repository;
    private final StringRedisTemplate redisTemplate;

    private static final String DRIVERS_CACHE_KEY = "available_drivers";

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("Warming up available drivers cache...");

        redisTemplate.delete(DRIVERS_CACHE_KEY);

        List<Long> availableIds = repository.findAllAvailableIds();

        if (!availableIds.isEmpty()) {
            String[] ids = availableIds.stream()
                    .map(String::valueOf)
                    .toArray(String[]::new);

            redisTemplate.opsForSet().add(DRIVERS_CACHE_KEY, ids);
            log.info("Cache warmed up. Drivers added: {}", ids.length);
        } else {
            log.info("Available drivers was not found. Cache is empty");
        }
    }
}
