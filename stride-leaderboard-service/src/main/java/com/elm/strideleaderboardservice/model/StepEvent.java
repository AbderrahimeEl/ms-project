package com.elm.strideleaderboardservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "step_events")
public class StepEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private int steps;
    private LocalDateTime eventTime;
    private LocalDateTime createdAt = LocalDateTime.now();

    public StepEvent(Long userId, Integer steps, LocalDateTime eventTime) {
        this.userId = userId;
        this.steps = steps;
        this.eventTime = eventTime;
    }
}
