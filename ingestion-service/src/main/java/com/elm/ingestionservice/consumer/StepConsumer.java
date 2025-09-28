package com.elm.ingestionservice.consumer;

import com.elm.ingestionservice.entity.StepEventEntity;
import com.elm.ingestionservice.repository.StepEventRepository;
import com.elm.ingestionservice.util.DataValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StepConsumer {

    private final StepEventRepository stepRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = {"min-steps-events"}, groupId = "ingestion")
    public void consumeStepEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String userId = jsonNode.get("userId").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            int steps = jsonNode.get("steps").asInt();

            if (!DataValidator.isValidSteps(steps)) {
                System.err.println("Invalid steps: " + steps);
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a", Locale.US);
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);

            // raw event to PostgreSQL (every minute)
            stepRepo.save(new StepEventEntity(null, userId, dateTime, steps));

            // Redis storage
            String today = LocalDate.now().toString();

            // Today's total steps
            String dailyKey = "steps:daily:" + userId + ":" + today;
            redisTemplate.opsForValue().increment(dailyKey, steps);
            redisTemplate.expire(dailyKey, 48, TimeUnit.HOURS);

            // Hourly breakdown (1-minute resolution)
            String hour = String.format("%02d", dateTime.getHour()); // "08", "09", etc.
            String minute = dateTime.format(DateTimeFormatter.ofPattern("HH:mm")); // "08:30", "08:31", etc.

            String minuteKey = "steps:minute:" + userId + ":" + today + ":" + minute;
            redisTemplate.opsForValue().set(minuteKey, String.valueOf(steps));
            redisTemplate.expire(minuteKey, 48, TimeUnit.HOURS);

            // Aggregate hourly
            String hourlyKey = "steps:hourly:" + userId + ":" + today + ":" + hour;
            redisTemplate.opsForValue().increment(hourlyKey, steps);
            redisTemplate.expire(hourlyKey, 48, TimeUnit.HOURS);

            // Active minutes detection (steps > 100 per minute = active)
            if (steps > 100) {
                String activeKey = "active:minutes:" + userId + ":" + today;
                String activeMinuteKey = "active:" + minute;
                if (!redisTemplate.opsForSet().isMember(activeKey, activeMinuteKey)) {
                    redisTemplate.opsForSet().add(activeKey, activeMinuteKey);
                    redisTemplate.expire(activeKey, 48, TimeUnit.HOURS);
                }
            }

            String leaderboardKey = "leaderboard:steps:" + today;
            redisTemplate.opsForZSet().incrementScore(leaderboardKey, userId, steps);

        } catch (Exception e) {
            System.err.println("Error processing step message: " + message);
            e.printStackTrace();
        }
    }
}