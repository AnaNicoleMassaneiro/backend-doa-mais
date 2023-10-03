package org.acme.Request;

import java.time.LocalTime;

public class AvailableTimeSlotRequest {
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}