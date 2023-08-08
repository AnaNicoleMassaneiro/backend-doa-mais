package org.acme.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HemobancoDateDTO {

    private List<AvailableDateDTO> availableDates;
    private Long hemobancoAddressId; // Mantenha esta linha

    // ... métodos getter e setter para availableDates ...

    public Long getHemobancoAddressId() {
        return hemobancoAddressId;
    }

    public void setHemobancoAddressId(Long hemobancoAddressId) {
        this.hemobancoAddressId = hemobancoAddressId;
    }
}