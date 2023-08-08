package org.acme.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "carddetails")
public class CardDetails extends PanacheEntity {
    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate validity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String bloodType;
}

