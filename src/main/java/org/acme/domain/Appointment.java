package org.acme.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hemobancoId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;


    @ManyToOne
    @JoinColumn(name = "hemobanco_Id") // Defina o nome da coluna estrangeira aqui
    private Hemobanco hemobanco;

    public Appointment(Long hemobancoId, Long userId, LocalDate date, LocalTime time) {
        this.hemobancoId = hemobancoId;
        this.userId = userId;
        this.date = date;
        this.time = time;
    }
}
