package com.elm.ingestionservice.util;

public class DataValidator {

    public static boolean isValidHeartrate(int hr) {
        return hr >= 30 && hr <= 220;
    }

    public static boolean isValidSteps(int steps) {
        return steps >= 0 && steps <= 250;
        // if you can do more than 250 steps in a min tell me
    }

    public static boolean isValidSleep(int minutes) {
        return minutes > 0 && minutes <= 24 * 60;
    }
}
