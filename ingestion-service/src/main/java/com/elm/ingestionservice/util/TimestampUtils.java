package com.elm.ingestionservice.util;

import com.google.protobuf.Timestamp;
import java.time.Instant;

public class TimestampUtils {
    public static Instant toInstant(Timestamp ts) {
        return Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos());
    }

    public static Timestamp toProto(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
