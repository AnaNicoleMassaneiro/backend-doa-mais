package org.acme.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AvailableDateDTO {

    private String date;
    private List<AvailableTimeSlotDTO> availableTimeSlots;
    private Long hemobancoAddressId; // Adicione esta linha

    // ... métodos getter e setter para date e availableTimeSlots ...

    public Long getHemobancoAddressId() {
        return hemobancoAddressId;
    }

    public void setHemobancoAddressId(Long hemobancoAddressId) {
        this.hemobancoAddressId = hemobancoAddressId;
    }
}
