package org.acme.Request;

import java.util.List;

public class CreateDateRequest {
    private List<AvailableDateRequest> availableDates;
    private Long hemobancoAddressId;

    public List<AvailableDateRequest> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<AvailableDateRequest> availableDates) {
        this.availableDates = availableDates;
    }

    public Long getHemobancoAddressId() {
        return hemobancoAddressId;
    }

    public void setHemobancoAddressId(Long hemobancoAddressId) {
        this.hemobancoAddressId = hemobancoAddressId;
    }
}
