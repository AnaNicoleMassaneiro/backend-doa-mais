package org.acme.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.domain.HemobancoDate;
import org.acme.repository.HemobancoDateRepository;
import java.util.List;

import java.util.Optional;

@ApplicationScoped
public class HemobancoDateService {

    @Inject
    HemobancoDateRepository dateRepository;

    public List<HemobancoDate> getAllDates() {
        return dateRepository.listAll();
    }

    public Optional<HemobancoDate> getDateById(Long id) {
        return dateRepository.findByIdOptional(id);
    }

    @Transactional
    public HemobancoDate saveDate(HemobancoDate date) {
        try {
            dateRepository.persist(date);
            return date;
        } catch (Exception e) {
            String errorMessage = "Erro ao salvar a data disponível.";
            throw new WebApplicationException(errorMessage, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteDate(Long id) {
        dateRepository.deleteById(id);
    }
}
