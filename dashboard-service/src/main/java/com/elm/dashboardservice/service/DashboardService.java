package com.elm.dashboardservice.service;

import com.elm.proto.*;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardServiceGrpc.DashboardServiceBlockingStub blockingStub;

    public Map<String, Object> getDashboardOverview(String userId) {
        try {
            DashboardRequest request = DashboardRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            DashboardOverview response = blockingStub.getDashboardOverview(request);

            return mapDashboardOverviewToMap(response);

        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for user {}: {}", userId, e.getStatus());
            throw new RuntimeException("Failed to fetch dashboard overview", e);
        }
    }

    public Map<String, Object> getTodayActivityCharts(String userId) {
        try {
            DashboardRequest request = DashboardRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            TodayActivityCharts response = blockingStub.getTodayActivityCharts(request);

            return mapTodayActivityChartsToMap(response);

        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for user {}: {}", userId, e.getStatus());
            throw new RuntimeException("Failed to fetch activity charts", e);
        }
    }

    public Map<String, Object> getWeeklyTrends(String userId, String weekStart) {
        try {
            WeeklyRequest request = WeeklyRequest.newBuilder()
                    .setUserId(userId)
                    .setWeekStart(weekStart)
                    .build();

            WeeklyTrends response = blockingStub.getWeeklyTrends(request);

            return mapWeeklyTrendsToMap(response);

        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for user {}: {}", userId, e.getStatus());
            throw new RuntimeException("Failed to fetch weekly trends", e);
        }
    }

    public Map<String, Object> getCompleteDashboard(String userId) {
        try {
            DashboardRequest request = DashboardRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            DashboardOverview overview = blockingStub.getDashboardOverview(request);
            TodayActivityCharts charts = blockingStub.getTodayActivityCharts(request);

            Map<String, Object> completeDashboard = new HashMap<>();
            completeDashboard.put("overview", mapDashboardOverviewToMap(overview));
            completeDashboard.put("charts", mapTodayActivityChartsToMap(charts));
            completeDashboard.put("userId", userId);
            completeDashboard.put("timestamp", java.time.LocalDateTime.now().toString());

            return completeDashboard;

        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for user {}: {}", userId, e.getStatus());
            throw new RuntimeException("Failed to fetch complete dashboard", e);
        }
    }


    private Map<String, Object> mapDashboardOverviewToMap(DashboardOverview overview) {
        Map<String, Object> result = new HashMap<>();

        result.put("userId", overview.getUserId());
        result.put("date", overview.getDate());
        result.put("lastUpdated", overview.getLastUpdated());

        RealTimeMetrics realTime = overview.getRealTime();
        Map<String, Object> realTimeMap = new HashMap<>();
        realTimeMap.put("currentHeartRate", realTime.getCurrentHeartRate());
        realTimeMap.put("todaySteps", realTime.getTodaySteps());
        realTimeMap.put("activeMinutes", realTime.getActiveMinutes());
        realTimeMap.put("caloriesBurned", realTime.getCaloriesBurned());
        realTimeMap.put("floorsClimbed", realTime.getFloorsClimbed());
        result.put("realTime", realTimeMap);

        SleepSummary sleep = overview.getSleepToday();
        Map<String, Object> sleepMap = new HashMap<>();
        sleepMap.put("totalMinutes", sleep.getTotalMinutes());
        sleepMap.put("asleepMinutes", sleep.getAsleepMinutes());
        sleepMap.put("restlessMinutes", sleep.getRestlessMinutes());
        sleepMap.put("awakeMinutes", sleep.getAwakeMinutes());
        sleepMap.put("efficiencyPercent", sleep.getEfficiencyPercent());
        sleepMap.put("currentSession", sleep.getCurrentSession());

        sleepMap.put("totalHours", String.format("%dh %dm",
                sleep.getTotalMinutes() / 60, sleep.getTotalMinutes() % 60));
        sleepMap.put("asleepHours", String.format("%dh %dm",
                sleep.getAsleepMinutes() / 60, sleep.getAsleepMinutes() % 60));

        result.put("sleep", sleepMap);

        ActivitySummary activity = overview.getActivityToday();
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("stepsGoal", activity.getStepsGoal());
        activityMap.put("stepsProgress", calculateProgress(realTime.getTodaySteps(), activity.getStepsGoal()));
        activityMap.put("activeGoal", activity.getActiveGoal());
        activityMap.put("activeProgress", calculateProgress(realTime.getActiveMinutes(), activity.getActiveGoal()));
        activityMap.put("caloriesGoal", activity.getCaloriesGoal());
        activityMap.put("caloriesProgress", calculateProgress(realTime.getCaloriesBurned(), activity.getCaloriesGoal()));
        result.put("goals", activityMap);

        return result;
    }

    private Map<String, Object> mapTodayActivityChartsToMap(TodayActivityCharts charts) {
        Map<String, Object> result = new HashMap<>();

        result.put("userId", charts.getUserId());
        result.put("date", charts.getDate());

        StepsChartData stepsChart = charts.getStepsChart();
        Map<String, Object> stepsMap = new HashMap<>();

        // Hourly steps for bar chart
        stepsMap.put("hourlyData", stepsChart.getHourlyStepsList().stream()
                .map(hourly -> {
                    Map<String, Object> hourMap = new HashMap<>();
                    hourMap.put("hour", hourly.getHour() + ":00");
                    hourMap.put("steps", hourly.getSteps());
                    hourMap.put("activeMinutes", hourly.getActiveMinutes());
                    return hourMap;
                })
                .toList());

        stepsMap.put("minuteData", stepsChart.getMinuteStepsList().stream()
                .map(minute -> {
                    Map<String, Object> minuteMap = new HashMap<>();
                    minuteMap.put("time", minute.getTime());
                    minuteMap.put("steps", minute.getSteps());
                    return minuteMap;
                })
                .toList());

        stepsMap.put("dailyTotal", stepsChart.getDailyTotal());
        stepsMap.put("dailyGoal", stepsChart.getDailyGoal());
        result.put("steps", stepsMap);

        HeartRateChartData hrChart = charts.getHeartRateChart();
        Map<String, Object> hrMap = new HashMap<>();

        hrMap.put("samples", hrChart.getSamplesList().stream()
                .map(sample -> {
                    Map<String, Object> sampleMap = new HashMap<>();
                    sampleMap.put("time", sample.getTime());
                    sampleMap.put("heartRate", sample.getHeartRate());
                    return sampleMap;
                })
                .toList());

        hrMap.put("dailyAverage", hrChart.getDailyAverage());
        hrMap.put("dailyMin", hrChart.getDailyMin());
        hrMap.put("dailyMax", hrChart.getDailyMax());
        hrMap.put("restingHeartRate", hrChart.getRestingHeartRate());
        result.put("heartRate", hrMap);

        // Sleep Timeline Chart
        SleepTimelineChart sleepChart = charts.getSleepChart();
        Map<String, Object> sleepMap = new HashMap<>();

        sleepMap.put("stages", sleepChart.getStagesList().stream()
                .map(stage -> {
                    Map<String, Object> stageMap = new HashMap<>();
                    stageMap.put("timeInterval", stage.getTimeInterval());
                    stageMap.put("stage", stage.getStage());
                    stageMap.put("durationMinutes", stage.getDurationMinutes());
                    return stageMap;
                })
                .toList());

        sleepMap.put("sessionId", sleepChart.getSessionId());
        sleepMap.put("startTime", sleepChart.getStartTime());
        sleepMap.put("endTime", sleepChart.getEndTime());
        sleepMap.put("efficiencyPercent", sleepChart.getEfficiencyPercent());
        result.put("sleep", sleepMap);

        return result;
    }

    private Map<String, Object> mapWeeklyTrendsToMap(WeeklyTrends trends) {
        Map<String, Object> result = new HashMap<>();

        result.put("userId", trends.getUserId());
        result.put("weekStart", trends.getWeekStart());

        // Steps Trends
        StepsTrends stepsTrends = trends.getStepsTrends();
        Map<String, Object> stepsMap = new HashMap<>();

        stepsMap.put("dailyData", stepsTrends.getDailyStepsList().stream()
                .map(daily -> {
                    Map<String, Object> dayMap = new HashMap<>();
                    dayMap.put("date", daily.getDate());
                    dayMap.put("steps", daily.getSteps());
                    dayMap.put("goalAchieved", daily.getGoalAchieved());
                    dayMap.put("activeMinutes", daily.getActiveMinutes());
                    return dayMap;
                })
                .toList());

        stepsMap.put("weeklyTotal", stepsTrends.getWeeklyTotal());
        stepsMap.put("weeklyAverage", stepsTrends.getWeeklyAverage());
        stepsMap.put("bestDaySteps", stepsTrends.getBestDaySteps());
        stepsMap.put("bestDayDate", stepsTrends.getBestDayDate());
        result.put("steps", stepsMap);

        HeartRateTrends hrTrends = trends.getHeartRateTrends();
        Map<String, Object> hrMap = new HashMap<>();

        hrMap.put("dailyData", hrTrends.getDailyHeartRatesList().stream()
                .map(daily -> {
                    Map<String, Object> dayMap = new HashMap<>();
                    dayMap.put("date", daily.getDate());
                    dayMap.put("avgHeartRate", daily.getAvgHeartRate());
                    dayMap.put("minHeartRate", daily.getMinHeartRate());
                    dayMap.put("maxHeartRate", daily.getMaxHeartRate());
                    dayMap.put("restingHeartRate", daily.getRestingHeartRate());
                    return dayMap;
                })
                .toList());

        hrMap.put("weeklyAvgResting", hrTrends.getWeeklyAvgResting());
        hrMap.put("weeklyAvgDaily", hrTrends.getWeeklyAvgDaily());
        result.put("heartRate", hrMap);

        // Sleep Trends
        SleepTrends sleepTrends = trends.getSleepTrends();
        Map<String, Object> sleepMap = new HashMap<>();

        sleepMap.put("dailyData", sleepTrends.getDailySleepList().stream()
                .map(daily -> {
                    Map<String, Object> dayMap = new HashMap<>();
                    dayMap.put("date", daily.getDate());
                    dayMap.put("totalMinutes", daily.getTotalMinutes());
                    dayMap.put("asleepMinutes", daily.getAsleepMinutes());
                    dayMap.put("restlessMinutes", daily.getRestlessMinutes());
                    dayMap.put("awakeMinutes", daily.getAwakeMinutes());
                    dayMap.put("efficiencyPercent", daily.getEfficiencyPercent());

                    dayMap.put("totalHours", String.format("%dh %dm",
                            daily.getTotalMinutes() / 60, daily.getTotalMinutes() % 60));
                    return dayMap;
                })
                .toList());

        sleepMap.put("weeklyAvgEfficiency", sleepTrends.getWeeklyAvgEfficiency());
        sleepMap.put("weeklyAvgDuration", sleepTrends.getWeeklyAvgDuration());
        sleepMap.put("bestSleepNight", sleepTrends.getBestSleepNight());
        result.put("sleep", sleepMap);

        return result;
    }

    private int calculateProgress(int current, int goal) {
        if (goal == 0) return 0;
        return Math.min(100, (int) ((double) current / goal * 100));
    }
}