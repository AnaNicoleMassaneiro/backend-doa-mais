package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.domain.HemobancoDate;
import org.acme.repository.HemobancoDateRepository;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class HemobancoDateService {

    @Inject
    HemobancoDateRepository dateRepository;

    @Inject
    HemobancoService hemobancoService;

    public List<HemobancoDate> getAllDates() {
        return dateRepository.listAll();
    }

    public HemobancoDate getDateById(Long id) {
        return dateRepository.findById(id);
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

    @Transactional
    public HemobancoDate updateDate(Long id, HemobancoDate updatedDate) {
        HemobancoDate date = dateRepository.findById(id);
        if (date == null) {
            throw new NotFoundException("Date with id " + id + " not found");
        }

        date.setAvailableDates(updatedDate.getAvailableDates());
        date.setHemobancoAddressId(updatedDate.getHemobancoAddressId());
        return dateRepository.getEntityManager().merge(date);
    }

    @Transactional
    public boolean isDateAvailableForHemobanco(Long hemobancoId, LocalDate selectedDate) {
        // Use the custom query from the HemobancoDateRepository to check date availability
        return dateRepository.isDateAvailableForHemobanco(hemobancoId, selectedDate);
    }

    public void deleteDate(Long id) {
        dateRepository.deleteById(id);
    }
}
