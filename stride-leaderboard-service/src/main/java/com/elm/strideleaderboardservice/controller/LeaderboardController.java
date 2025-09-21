package com.elm.strideleaderboardservice.controller;


import com.elm.strideleaderboardservice.model.DailyStepTotal;
import com.elm.strideleaderboardservice.model.LeaderboardEntry;
import com.elm.strideleaderboardservice.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        return leaderboardService.getLeaderboard(limit);
    }

    @GetMapping("/users/{userId}/history")
    public List<DailyStepTotal> getStepHistory(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return leaderboardService.getStepHistory(userId, startDate, endDate);
    }
}