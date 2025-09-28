package com.elm.ingestionservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sleep_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private LocalDateTime timestamp;
    private int state;
    private String sessionId;
}
