package server;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Item {

    String value;

    LocalDateTime createdTime;

    Integer expiredTime; // ms

    public Item() {}

    public Item(String value) {
        this.value = value;
        this.createdTime = LocalDateTime.now();
        this.expiredTime = 0;
    }

    public Item(String value, Integer expiredTime) {
        this.value = value;
        this.createdTime = LocalDateTime.now();
        this.expiredTime = expiredTime;
    }

    public boolean isExpired() {
        return createdTime.plus(expiredTime, ChronoUnit.MILLIS).isBefore(LocalDateTime.now());
    }
}
