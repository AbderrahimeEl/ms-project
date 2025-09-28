package com.elm.dashboardservice.controller;

import com.elm.dashboardservice.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview/{userId}")
    public ResponseEntity<Map<String, Object>> getDashboardOverview(@PathVariable String userId) {
        try {
            Map<String, Object> overview = dashboardService.getDashboardOverview(userId);
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch dashboard overview: " + e.getMessage()));
        }
    }

    @GetMapping("/charts/{userId}")
    public ResponseEntity<Map<String, Object>> getTodayActivityCharts(@PathVariable String userId) {
        try {
            Map<String, Object> charts = dashboardService.getTodayActivityCharts(userId);
            return ResponseEntity.ok(charts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch activity charts: " + e.getMessage()));
        }
    }

    @GetMapping("/weekly/{userId}")
    public ResponseEntity<Map<String, Object>> getWeeklyTrends(
            @PathVariable String userId,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate weekStart) {

        try {
            if (weekStart == null) {
                weekStart = LocalDate.now().minusDays(6);
            }

            Map<String, Object> trends = dashboardService.getWeeklyTrends(userId, weekStart.toString());
            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch weekly trends: " + e.getMessage()));
        }
    }

    @GetMapping("/complete/{userId}")
    public ResponseEntity<Map<String, Object>> getCompleteDashboard(@PathVariable String userId) {
        try {
            Map<String, Object> completeDashboard = dashboardService.getCompleteDashboard(userId);
            return ResponseEntity.ok(completeDashboard);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to fetch complete dashboard: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "OK", "service", "dashboard-service"));
    }
}