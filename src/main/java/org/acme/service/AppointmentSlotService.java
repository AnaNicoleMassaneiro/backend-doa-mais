package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.AppointmentSlot;
import org.acme.repository.AppointmentSlotRepository;

import java.util.List;

@ApplicationScoped
public class AppointmentSlotService {

    @Inject
    AppointmentSlotRepository appointmentSlotRepository;

    public List<AppointmentSlot> getAllAppointmentSlots() {
        return appointmentSlotRepository.listAll();
    }

    @Transactional
    public void createAppointmentSlot(AppointmentSlot appointmentSlot) {
        appointmentSlotRepository.persist(appointmentSlot);
    }

}