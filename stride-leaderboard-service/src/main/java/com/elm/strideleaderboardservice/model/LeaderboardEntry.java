package com.elm.strideleaderboardservice.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class LeaderboardEntry {
    private int rank;
    private int userId;
    private double totalSteps;
}
