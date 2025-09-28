package com.elm.ingestionservice.consumer;

import com.elm.ingestionservice.entity.SleepEventEntity;
import com.elm.ingestionservice.repository.SleepEventRepository;
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
public class SleepConsumer {

    private final SleepEventRepository sleepRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sleep-events", groupId = "ingestion")
    public void consumeSleepEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            String userId = jsonNode.get("userId").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            int state = jsonNode.get("state").asInt();
            String sessionId = jsonNode.get("sessionId").asText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a", Locale.US);
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
            String today = LocalDate.now().toString();

            // Save raw event to PostgreSQL
            sleepRepo.save(new SleepEventEntity(null, userId, dateTime, state, sessionId));

            // Redis storage for dashboard
            String dailyKey = "sleep:daily:" + userId + ":" + today;

            String stateName = getStateName(state);
            redisTemplate.opsForHash().increment(dailyKey, stateName, 1);
            redisTemplate.expire(dailyKey, 48, TimeUnit.HOURS);

            // Sleep timeline for charts
            String fiveMinuteInterval = getFiveMinuteInterval(dateTime); // "22:30", "22:35", etc.
            String timelineKey = "sleep:timeline:" + userId + ":" + sessionId;

            // Store the sleep state for this 5-minute interval
            redisTemplate.opsForHash().put(timelineKey, fiveMinuteInterval, stateName);
            redisTemplate.expire(timelineKey, 48, TimeUnit.HOURS);

            String sessionKey = "sleep:session:" + userId + ":" + sessionId;
            if (!redisTemplate.hasKey(sessionKey)) {
                redisTemplate.opsForHash().put(sessionKey, "start_time",
                        dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                redisTemplate.opsForHash().put(sessionKey, "date", today);
            }
            // Update last activity and calculate duration
            redisTemplate.opsForHash().put(sessionKey, "last_activity",
                    dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            redisTemplate.expire(sessionKey, 48, TimeUnit.HOURS);

            // Current sleep session
            String currentSessionKey = "sleep:current:" + userId;
            redisTemplate.opsForValue().set(currentSessionKey, sessionId);
            redisTemplate.expire(currentSessionKey, 12, TimeUnit.HOURS);

            // Sleep efficiency
            updateSleepEfficiency(userId, sessionId, state);

            if (state == 1) {
                String leaderboardKey = "leaderboard:sleep:" + today;
                redisTemplate.opsForZSet().incrementScore(leaderboardKey, userId, 1);
            }

        } catch (Exception e) {
            System.err.println("Error processing sleep message: " + message);
            e.printStackTrace();
        }
    }

    private String getStateName(int state) {
        return switch (state) {
            case 1 -> "asleep";
            case 2 -> "restless";
            case 3 -> "awake";
            default -> "unknown";
        };
    }

    private String getFiveMinuteInterval(LocalDateTime dateTime) {
        int minute = dateTime.getMinute();
        int fiveMinuteBlock = (minute / 5) * 5; // 0, 5, 10, 15, ..
        return dateTime.format(DateTimeFormatter.ofPattern("HH:")) +
                String.format("%02d", fiveMinuteBlock);
    }

    private void updateSleepEfficiency(String userId, String sessionId, int state) {
        String efficiencyKey = "sleep:efficiency:" + userId + ":" + sessionId;

        if (state == 1) {
            redisTemplate.opsForHash().increment(efficiencyKey, "asleep_minutes", 1);
        }
        if (state >= 1 && state <= 3) {
            redisTemplate.opsForHash().increment(efficiencyKey, "total_minutes", 1);
        }

        redisTemplate.expire(efficiencyKey, 48, TimeUnit.HOURS);
    }
}