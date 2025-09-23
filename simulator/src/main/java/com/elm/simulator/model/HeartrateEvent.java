package com.elm.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartrateEvent {
    private String userId;
    private String timestamp;
    private int heartrate;
}
