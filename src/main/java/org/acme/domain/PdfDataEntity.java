package org.acme.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PdfDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_bytes", nullable = false)
    private byte[] pdfBytes;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

}

