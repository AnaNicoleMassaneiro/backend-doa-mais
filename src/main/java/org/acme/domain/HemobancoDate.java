package org.acme.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hemobanco_date")
public class  HemobancoDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hemobanco_date_id")
    private List<AvailableDateEntity> availableDates;

    private Long hemobancoAddressId;

    @ManyToOne
    @JoinColumn(name = "hemobanco_address_id")
    private Hemobanco hemobancoAddress;

    public void setHemobancoAddress(Hemobanco hemobanco) {
        this.hemobancoAddress = hemobanco;
    }

    public Long getHemobancoAddressId() {
        return hemobancoAddressId;
    }
}