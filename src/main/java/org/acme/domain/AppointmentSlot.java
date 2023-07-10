package org.acme.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "appointment")
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime time;

    // Construtores, getters e setters

    public AppointmentSlot() {
    }

    public AppointmentSlot(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    // Getters e setters omitidos para brevidade

    // Implemente os getters e setters
}
