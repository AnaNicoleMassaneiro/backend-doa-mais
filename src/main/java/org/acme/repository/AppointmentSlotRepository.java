package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.AppointmentSlot;

@ApplicationScoped
public class AppointmentSlotRepository implements PanacheRepository<AppointmentSlot> {

}