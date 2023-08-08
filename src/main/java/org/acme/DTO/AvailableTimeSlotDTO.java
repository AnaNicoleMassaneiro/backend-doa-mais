package org.acme.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableTimeSlotDTO {

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}