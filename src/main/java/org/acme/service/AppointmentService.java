package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.acme.domain.Appointment;
import org.acme.repository.AppointmentRepository;

import java.util.List;

@ApplicationScoped
public class AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    EntityManager entityManager;

    public Appointment scheduleAppointment(Appointment hemobanco) {
        Appointment appointment = new Appointment(hemobanco.getHemobancoId(), hemobanco.getUserId(), hemobanco.getDate(), hemobanco.getTime());
        appointmentRepository.persist(appointment);
        return appointment;
    }

    public List<Appointment> getAppointmentsByUser(Long userId) {
        TypedQuery<Appointment> query = entityManager.createQuery(
                "SELECT a FROM Appointment a WHERE a.userId = :userId", Appointment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
