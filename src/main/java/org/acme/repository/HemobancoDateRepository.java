package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import org.acme.domain.HemobancoDate;

import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class HemobancoDateRepository implements PanacheRepository<HemobancoDate> {

    @PersistenceContext
    EntityManager entityManager;

    // Custom query to check if a date is already available for a specific Hemobanco
    public boolean isDateAvailableForHemobanco(Long hemobancoId, LocalDate date) {
        return find("hemobancoAddressId = ?1 and date = ?2", hemobancoId, date).count() == 0;
    }
}
