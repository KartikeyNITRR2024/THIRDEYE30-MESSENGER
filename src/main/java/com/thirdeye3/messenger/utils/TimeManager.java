package com.thirdeye3.messenger.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimeManager {
    
    @Value("${thirdeye.timezone}")
    private String timeZone;

    public Timestamp getCurrentTime() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of(timeZone));
        LocalDateTime localDateTime = currentTime.toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }
}
