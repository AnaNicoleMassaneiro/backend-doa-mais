package org.acme.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hemobanco_date")
public class HemobancoDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hemobanco_date_id")
    private List<AvailableDateEntity> availableDates;

    // Getters and setters...

    // Constructors...
}
