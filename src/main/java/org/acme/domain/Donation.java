package org.acme.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Donation {
    private Long id;
    private LocalDate dateDonation;
    private Integer cardNumberDonor;
    private Integer hemobanco;
}