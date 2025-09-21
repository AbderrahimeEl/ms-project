package com.elm.strideleaderboardservice.consumer;

import com.elm.strideleaderboardservice.model.StepEvent;
import com.elm.strideleaderboardservice.repository.StepEventRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Component
public class StepEventConsumer {
    private RedisTemplate<String, Object> redisTemplate;
    private StepEventRepository stepEventRepository;

    @KafkaListener(topics = "my-topic", groupId = "leaderboard-group")
    public void consume(String message) {
        try {
            JSONObject event = new JSONObject(message);
            long userId = event.getLong("user_id");
            int steps = event.getInt("steps");
            long timestamp = event.getLong("timestamp");

            LocalDateTime eventTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(timestamp),
                    ZoneId.systemDefault()
            );

            updateLeaderboard(userId, steps);
            saveStepEvent(userId, steps, eventTime);

            System.out.println("Processed step event for user " + userId + ": " + steps + " steps");
        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void updateLeaderboard(long userId, int steps) {
        redisTemplate.opsForZSet().incrementScore("leaderboard", String.valueOf(userId), steps);
    }
    private void saveStepEvent(Long userId, Integer steps, LocalDateTime eventTime) {
        StepEvent stepEvent = new StepEvent(userId,steps,eventTime);
        stepEventRepository.save(stepEvent);
    }
}
