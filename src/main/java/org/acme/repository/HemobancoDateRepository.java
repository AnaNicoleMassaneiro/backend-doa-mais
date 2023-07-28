package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.HemobancoDate;

@ApplicationScoped
public class HemobancoDateRepository implements PanacheRepository<HemobancoDate> {
}