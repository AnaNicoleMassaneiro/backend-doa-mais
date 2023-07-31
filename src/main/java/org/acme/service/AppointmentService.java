package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.acme.domain.Appointment;
import org.acme.exception.UserHasAppointmentsException;
import org.acme.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    EntityManager entityManager;


    @Transactional
    public Appointment scheduleAppointment(Appointment appointment) throws UserHasAppointmentsException {
        Long userId = appointment.getUserId();

        List<Appointment> userAppointments = getAppointmentsByUser(userId);

        if(userAppointments.isEmpty()) {
            appointmentRepository.persist(appointment);
        } else {
            throw new UserHasAppointmentsException("User with ID " + userId + " already has appointments scheduled.");
        }

        return appointment;
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

        // Query the database to get the next appointment for the user
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
