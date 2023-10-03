package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.acme.domain.Appointment;

import java.util.List;

@ApplicationScoped
public class AppointmentRepository implements PanacheRepository<Appointment> {
    private final EntityManager em;

    public AppointmentRepository(EntityManager em) {
        this.em = em;
    }

    public List<Appointment> getAppointmentsByHemobanco(Long hemobancoId) {
        return em.createQuery("SELECT a FROM Appointment a WHERE a.hemobancoId = :hemobancoId", Appointment.class)
                .setParameter("hemobancoId", hemobancoId)
                .getResultList();
    }
}