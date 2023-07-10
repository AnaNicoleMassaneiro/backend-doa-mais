package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Hemobanco;

@ApplicationScoped
public class HemobancoRepository implements PanacheRepository<Hemobanco> {

}