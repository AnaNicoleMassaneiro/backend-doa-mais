package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.Appointment;
import org.acme.repository.AppointmentRepository;

@ApplicationScoped
public class AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    public Appointment scheduleAppointment(Appointment hemobanco) {
        Appointment appointment = new Appointment(hemobanco.getHemobancoId(), hemobanco.getUserId(), hemobanco.getDate(), hemobanco.getTime());
        appointmentRepository.persist(appointment);
        return appointment;
    }

    // Add other methods as needed
}
