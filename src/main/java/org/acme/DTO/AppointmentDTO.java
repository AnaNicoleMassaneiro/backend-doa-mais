package org.acme.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentDTO {
    private Long id;
    private Long hemobancoId;
    private Long userId;
    private LocalDate date;
    private LocalTime time;
    private String hemobancoNome;
    private String hemobancoEndereco;

    // Construtores, getters e setters aqui
}
