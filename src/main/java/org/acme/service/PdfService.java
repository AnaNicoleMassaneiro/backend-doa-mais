package org.acme.service;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.acme.domain.Appointment;
import org.acme.domain.PdfDataEntity;
import org.acme.repository.PdfDataRepository;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@ApplicationScoped
@RegisterForReflection
public class PdfService {

    @Inject
    PdfDataRepository pdfDataRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void savePdfForAppointment(InputStream pdfFileInputStream, Appointment appointment) throws IOException {
        byte[] pdfBytes = IOUtils.toByteArray(pdfFileInputStream);

        PdfDataEntity pdfDataEntity = new PdfDataEntity();
        pdfDataEntity.setPdfBytes(pdfBytes);
        pdfDataEntity.setAppointment(appointment); // Associa o PDF ao agendamento

        pdfDataRepository.persist(pdfDataEntity);
    }

    public boolean hasPdfForAppointment(Long appointmentId) {
        Long count = entityManager.createQuery("SELECT COUNT(p) FROM PdfDataEntity p WHERE p.appointment.id = :appointmentId", Long.class)
                .setParameter("appointmentId", appointmentId)
                .getSingleResult();

        return count > 0;
    }

    public InputStream downloadPdfForAppointment(Long appointmentId) {
        TypedQuery<PdfDataEntity> query = entityManager.createQuery(
                "SELECT p FROM PdfDataEntity p WHERE p.appointment.id = :appointmentId", PdfDataEntity.class);
        query.setParameter("appointmentId", appointmentId);

        List<PdfDataEntity> pdfDataEntities = query.getResultList();

        if (!pdfDataEntities.isEmpty()) {
            PdfDataEntity pdfDataEntity = pdfDataEntities.get(0);
            return new ByteArrayInputStream(pdfDataEntity.getPdfBytes());
        } else {
            return null;
        }
    }



    private String getFilePathForAppointment(Long appointmentId) {
        // Implemente a lógica para obter o caminho do arquivo PDF associado ao agendamento
        // Retorne o caminho do arquivo PDF com base no appointmentId.
        // Substitua esta lógica com a sua implementação real.

        // Aqui, usamos um exemplo em que todos os PDFs têm o mesmo nome e estão na mesma pasta.
        String pdfFileName = "appointment_" + appointmentId + ".pdf";
        String pdfFilePath = "/caminho/do/seu/diretorio/pdfs/" + pdfFileName;

        return pdfFilePath;
    }


}