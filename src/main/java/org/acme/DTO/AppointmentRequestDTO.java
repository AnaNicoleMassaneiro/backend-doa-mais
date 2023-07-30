package org.acme.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
public class AppointmentRequestDTO {
    private Long hemobancoId;
    private Long userId;
    private LocalDate date;
    private LocalTime time;
}