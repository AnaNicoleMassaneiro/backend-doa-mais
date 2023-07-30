package org.acme.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "available_time_slot")
public class AvailableTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time")
    private LocalTime time;

    // Add any other relevant properties or associations here
}
