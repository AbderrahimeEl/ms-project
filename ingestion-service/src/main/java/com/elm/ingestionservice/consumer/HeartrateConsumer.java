package com.elm.ingestionservice.consumer;

import com.elm.ingestionservice.entity.HeartrateEventEntity;
import com.elm.ingestionservice.repository.HeartrateEventRepository;
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
public class HeartrateConsumer {

    private final HeartrateEventRepository hrRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "heartrate-events", groupId = "ingestion")
    public void consumeHeartrateEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String userId = jsonNode.get("userId").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            int heartrate = jsonNode.get("heartrate").asInt();

            if (!DataValidator.isValidHeartrate(heartrate)) {
                System.err.println("Invalid heartrate: " + heartrate);
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a", Locale.US);
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);

            // Save raw event to PostgreSQL
            hrRepo.save(new HeartrateEventEntity(null, userId, dateTime, heartrate));

            // Redis storage for dashboard
            String today = LocalDate.now().toString();

            //  Latest heartrate
            redisTemplate.opsForValue().set("heartrate:latest:" + userId, String.valueOf(heartrate));

            //  Time-series with 1-minute resolution
            String minuteKey = dateTime.format(DateTimeFormatter.ofPattern("HH:mm")); // "08:30", "08:31", etc.
            String timeSeriesKey = "heartrate:timeseries:" + userId + ":" + today;

            redisTemplate.opsForHash().put(timeSeriesKey, minuteKey, String.valueOf(heartrate));

            // Set expiration
            redisTemplate.expire(timeSeriesKey, 48, TimeUnit.HOURS);

            //  Daily statistics
            String statsKey = "heartrate:daily:" + userId + ":" + today;
            redisTemplate.opsForList().rightPush(statsKey, String.valueOf(heartrate));
            redisTemplate.expire(statsKey, 48, TimeUnit.HOURS);

            String avgKey = "heartrate_avg:" + userId + ":" + today;
            redisTemplate.opsForList().rightPush(avgKey, String.valueOf(heartrate));
            redisTemplate.expire(avgKey, 48, TimeUnit.HOURS);

        } catch (Exception e) {
            System.err.println("Error processing heartrate message: " + message);
            e.printStackTrace();
        }
    }
}