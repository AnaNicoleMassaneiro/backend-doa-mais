package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.PdfDataEntity;

@ApplicationScoped
public class PdfDataRepository implements PanacheRepository<PdfDataEntity> {
}