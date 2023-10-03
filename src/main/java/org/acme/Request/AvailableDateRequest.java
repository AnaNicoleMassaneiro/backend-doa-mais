package org.acme.Request;

import java.time.LocalDate;
import java.util.List;

public class AvailableDateRequest {
    private LocalDate date;
    private List<AvailableTimeSlotRequest> availableTimeSlots;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<AvailableTimeSlotRequest> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<AvailableTimeSlotRequest> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}