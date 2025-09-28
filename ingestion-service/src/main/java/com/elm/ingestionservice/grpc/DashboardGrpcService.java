package com.elm.ingestionservice.grpc;

import com.elm.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class DashboardGrpcService extends DashboardServiceGrpc.DashboardServiceImplBase {

    private final RedisTemplate<String, String> redisTemplate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void getDashboardOverview(DashboardRequest request,
                                     StreamObserver<DashboardOverview> responseObserver) {
        try {
            String userId = request.getUserId();
            String today = LocalDate.now().format(dateFormatter);

            DashboardOverview response = DashboardOverview.newBuilder()
                    .setUserId(userId)
                    .setDate(today)
                    .setLastUpdated(LocalDateTime.now().toString())
                    .setRealTime(getRealTimeMetrics(userId, today))
                    .setSleepToday(getSleepSummary(userId, today))
                    .setActivityToday(getActivitySummary(userId, today))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getTodayActivityCharts(DashboardRequest request,
                                       StreamObserver<TodayActivityCharts> responseObserver) {
        try {
            String userId = request.getUserId();
            String today = LocalDate.now().format(dateFormatter);

            TodayActivityCharts response = TodayActivityCharts.newBuilder()
                    .setUserId(userId)
                    .setDate(today)
                    .setStepsChart(getStepsChartData(userId, today))
                    .setHeartRateChart(getHeartRateChartData(userId, today))
                    .setSleepChart(getSleepTimelineChart(userId, today))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getWeeklyTrends(WeeklyRequest request,
                                StreamObserver<WeeklyTrends> responseObserver) {
        try {
            String userId = request.getUserId();
            LocalDate weekStart = LocalDate.parse(request.getWeekStart());

            WeeklyTrends response = WeeklyTrends.newBuilder()
                    .setUserId(userId)
                    .setWeekStart(request.getWeekStart())
                    .setStepsTrends(getStepsTrends(userId, weekStart))
                    .setHeartRateTrends(getHeartRateTrends(userId, weekStart))
                    .setSleepTrends(getSleepTrends(userId, weekStart))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    // helpers

    private RealTimeMetrics getRealTimeMetrics(String userId, String today) {
        // Get latest heart rate
        String currentHr = redisTemplate.opsForValue().get("heartrate:latest:" + userId);

        // Get today's steps
        String dailyStepsKey = "steps:daily:" + userId + ":" + today;
        String todaySteps = redisTemplate.opsForValue().get(dailyStepsKey);

        // Get active minutes
        String activeKey = "active:minutes:" + userId + ":" + today;
        Long activeMinutes = redisTemplate.opsForSet().size(activeKey);

        return RealTimeMetrics.newBuilder()
                .setCurrentHeartRate(currentHr != null ? Integer.parseInt(currentHr) : 0)
                .setTodaySteps(todaySteps != null ? Integer.parseInt(todaySteps) : 0)
                .setActiveMinutes(activeMinutes != null ? activeMinutes.intValue() : 0)
                .build();
    }

    private SleepSummary getSleepSummary(String userId, String today) {
        String sleepKey = "sleep:daily:" + userId + ":" + today;
        Map<Object, Object> sleepData = redisTemplate.opsForHash().entries(sleepKey);

        int asleep = Integer.parseInt(sleepData.getOrDefault("asleep", "0").toString());
        int restless = Integer.parseInt(sleepData.getOrDefault("restless", "0").toString());
        int awake = Integer.parseInt(sleepData.getOrDefault("awake", "0").toString());
        int total = asleep + restless + awake;

        double efficiency = total > 0 ? (double) asleep / total * 100 : 0;

        return SleepSummary.newBuilder()
                .setTotalMinutes(total)
                .setAsleepMinutes(asleep)
                .setRestlessMinutes(restless)
                .setAwakeMinutes(awake)
                .setEfficiencyPercent(efficiency)
                .build();
    }

    private StepsChartData getStepsChartData(String userId, String today) {
        // Get hourly steps
        List<HourlySteps> hourlySteps = new ArrayList<>();
        int dailyTotal = 0;

        for (int hour = 0; hour < 24; hour++) {
            String hourStr = String.format("%02d", hour);
            String key = "steps:hourly:" + userId + ":" + today + ":" + hourStr;
            String steps = redisTemplate.opsForValue().get(key);
            int hourSteps = steps != null ? Integer.parseInt(steps) : 0;
            dailyTotal += hourSteps;

            hourlySteps.add(HourlySteps.newBuilder()
                    .setHour(hourStr)
                    .setSteps(hourSteps)
                    .build());
        }

        return StepsChartData.newBuilder()
                .addAllHourlySteps(hourlySteps)
                .setDailyTotal(dailyTotal)
                .setDailyGoal(10000) // hard coded hh
                .build();
    }

    private HeartRateChartData getHeartRateChartData(String userId, String today) {
        String timeSeriesKey = "heartrate:timeseries:" + userId + ":" + today;
        Map<Object, Object> hrData = redisTemplate.opsForHash().entries(timeSeriesKey);

        List<HeartRateSample> samples = new ArrayList<>();
        List<Integer> allRates = new ArrayList<>();

        hrData.forEach((time, hr) -> {
            int heartRate = Integer.parseInt(hr.toString());
            samples.add(HeartRateSample.newBuilder()
                    .setTime(time.toString())
                    .setHeartRate(heartRate)
                    .build());
            allRates.add(heartRate);
        });

        // Calculate stats
        int avg = allRates.isEmpty() ? 0 : (int) allRates.stream().mapToInt(Integer::intValue).average().orElse(0);
        int min = allRates.isEmpty() ? 0 : allRates.stream().mapToInt(Integer::intValue).min().orElse(0);
        int max = allRates.isEmpty() ? 0 : allRates.stream().mapToInt(Integer::intValue).max().orElse(0);

        return HeartRateChartData.newBuilder()
                .addAllSamples(samples)
                .setDailyAverage(avg)
                .setDailyMin(min)
                .setDailyMax(max)
                .build();
    }


    private ActivitySummary getActivitySummary(String userId, String today) {
        // i need to make these configurable but i'm too lazy
        return ActivitySummary.newBuilder()
                .setStepsGoal(10000)
                .setActiveGoal(30)
                .setCaloriesGoal(500)
                .build();
    }

    private SleepTimelineChart getSleepTimelineChart(String userId, String today) {
        return SleepTimelineChart.newBuilder().build();
    }

    private StepsTrends getStepsTrends(String userId, LocalDate weekStart) {
        // Implementation for weekly steps trends
        return StepsTrends.newBuilder().build();
    }

    private HeartRateTrends getHeartRateTrends(String userId, LocalDate weekStart) {
        // Implementation for weekly heart rate trends
        return HeartRateTrends.newBuilder().build();
    }

    private SleepTrends getSleepTrends(String userId, LocalDate weekStart) {
        // Implementation for weekly sleep trends
        return SleepTrends.newBuilder().build();
    }
}