package com.elm.strideleaderboardservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DailyStepTotal {
    private LocalDate date;
    private Integer totalSteps;
}
