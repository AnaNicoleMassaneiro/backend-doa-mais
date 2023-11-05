package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.acme.domain.Appointment;
import org.acme.domain.Hemobanco;
import org.acme.domain.PdfDataEntity;
import org.acme.exception.AppointmentNotFoundException;
import org.acme.exception.UserHasAppointmentsException;
import org.acme.repository.AppointmentRepository;
import org.acme.repository.PdfDataRepository;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    PdfDataRepository pdfDataRepository;

    public List<Appointment> getAllAppointment() {
        String jpql = "SELECT a FROM Appointment a";
        TypedQuery<Appointment> query = entityManager.createQuery(jpql, Appointment.class);

        return query.getResultList();
    }

    @Transactional
    public Appointment scheduleAppointment(Appointment appointment) throws UserHasAppointmentsException {
        Long userId = appointment.getUserId();
        Long hemobancoId = appointment.getHemobancoId();

        List<Appointment> userAppointments = getAppointmentsByUser(userId);
        Hemobanco hemobancoAppointments = getHemobancoById(hemobancoId);

        if (userAppointments.isEmpty()) {
            appointment.setHemobanco(hemobancoAppointments);
            appointment.setCompleted(false); // Defina como "false" ao criar o agendamento
            appointmentRepository.persist(appointment);
        } else {
            throw new UserHasAppointmentsException("User with ID " + userId + " already has appointments scheduled.");
        }

        return appointment;
    }


    public Hemobanco getHemobancoById(Long id) {
        String jpql = "SELECT a FROM Hemobanco a WHERE a.id = :id";
        TypedQuery<Hemobanco> query = entityManager.createQuery(jpql, Hemobanco.class);
        query.setParameter("id", id);

        return query.getResultList().get(0);
    }

    public List<Appointment> getAppointmentsWithHemobancoByUser(Long userId) {
        String jpql = "SELECT a FROM Appointment a JOIN FETCH a.hemobanco WHERE a.userId = :userId";
        TypedQuery<Appointment> query = entityManager.createQuery(jpql, Appointment.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Transactional
    public int cancelAppointmentByUserId(Long userId) {
        List<Appointment> userAppointments = getAppointmentsByUser(userId);
        int canceledCount = 0;

        for (Appointment appointment : userAppointments) {
            appointmentRepository.delete(appointment);
            canceledCount++;
        }

        return canceledCount;
    }

    public List<Appointment> getAppointmentsByUser(Long userId) {
        TypedQuery<Appointment> query = entityManager.createQuery(
                "SELECT a FROM Appointment a WHERE a.userId = :userId", Appointment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Appointment getNextAppointmentForUser(Long userId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Appointment> appointments = entityManager.createQuery(
                        "SELECT a FROM Appointment a WHERE a.userId = :userId " +
                                "AND (a.date > :currentDate OR (a.date = :currentDate AND a.time > :currentTime)) " +
                                "ORDER BY a.date, a.time ASC", Appointment.class)
                .setParameter("userId", userId)
                .setParameter("currentDate", currentDate)
                .setParameter("currentTime", currentTime)
                .getResultList();

        if (!appointments.isEmpty()) {
            return appointments.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public void markAppointmentAsCompleted(Appointment appointment) {
        if (appointment != null) {
            appointment.setCompleted(true);
            appointmentRepository.persist(appointment);
        } else {
            throw new AppointmentNotFoundException("Appointment not found.");
        }
    }

    @Transactional
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId); // Use o método estático findById do Panache
    }

    @Transactional
    public void savePdf(InputStream pdfFileInputStream) throws IOException {
        byte[] pdfBytes = IOUtils.toByteArray(pdfFileInputStream);

        PdfDataEntity pdfDataEntity = new PdfDataEntity();
        pdfDataEntity.setPdfBytes(pdfBytes);

        pdfDataRepository.persist(pdfDataEntity);
    }

    public Appointment findAppointmentById(Long id) {
        return entityManager.find(Appointment.class, id);
    }


}
