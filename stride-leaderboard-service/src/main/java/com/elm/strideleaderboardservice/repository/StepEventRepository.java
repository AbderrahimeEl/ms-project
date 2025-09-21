package com.elm.strideleaderboardservice.repository;

import com.elm.strideleaderboardservice.model.StepEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StepEventRepository extends CrudRepository<StepEvent, Long> {
    @Query("SELECT new map(FUNCTION('DATE', se.eventTime) as date, SUM(se.steps) as totalSteps) " +
            "FROM StepEvent se " +
            "WHERE se.userId = :userId AND se.eventTime BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', se.eventTime) " +
            "ORDER BY date")
    List<Map<String, Object>> findDailyTotals(@Param("userId") Integer userId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}
