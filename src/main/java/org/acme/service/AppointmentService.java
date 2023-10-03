package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.acme.domain.Appointment;
import org.acme.domain.Hemobanco;
import org.acme.exception.UserHasAppointmentsException;
import org.acme.repository.AppointmentRepository;

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

        if(userAppointments.isEmpty()) {
            appointment.setHemobanco(hemobancoAppointments);
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
            // Return the first appointment from the list (the next appointment)
            return appointments.get(0);
        } else {
            // If no upcoming appointments are found, return null
            return null;
        }
    }


}
