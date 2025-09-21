package com.elm.strideleaderboardservice.service;

import com.elm.strideleaderboardservice.model.DailyStepTotal;
import com.elm.strideleaderboardservice.model.LeaderboardEntry;
import com.elm.strideleaderboardservice.repository.StepEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Service
public class LeaderboardService {



    private RedisTemplate<String, String> redisTemplate;
    private StepEventRepository stepEventRepository;

    public List<LeaderboardEntry> getLeaderboard(int limit) {
        // Get top users from Redis sorted set
        Set<ZSetOperations.TypedTuple<String>> topUsers =
                redisTemplate.opsForZSet().reverseRangeWithScores("leaderboard", 0, limit - 1);

        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        if (topUsers != null) {
            int rank = 1;
            for (ZSetOperations.TypedTuple<String> user : topUsers) {
                leaderboard.add(new LeaderboardEntry(
                        rank++,
                        Integer.parseInt(user.getValue()),
                        user.getScore()
                ));
            }
        }
        return leaderboard;
    }

    public List<DailyStepTotal> getStepHistory(Integer userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<Map<String, Object>> dailyTotals =
                stepEventRepository.findDailyTotals(userId, startDateTime, endDateTime);

        List<DailyStepTotal> result = new ArrayList<>();
        for (Map<String, Object> dailyTotal : dailyTotals) {
            java.sql.Date sqlDate = (java.sql.Date) dailyTotal.get("date");
            Long totalSteps = (Long) dailyTotal.get("totalSteps");

            result.add(new DailyStepTotal(
                    sqlDate.toLocalDate(),
                    totalSteps.intValue()
            ));
        }
        return result;
    }
}