package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.acme.domain.Hemobanco;
import org.acme.repository.HemobancoRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class HemobancoService {

    @Inject
    HemobancoRepository hemobancoRepository;

    public List<Hemobanco> getAllHemobancos() {
        return hemobancoRepository.listAll();
    }

    @Transactional
    public Hemobanco saveHemobanco(Hemobanco hemobanco) {
        hemobancoRepository.persist(hemobanco);
        return hemobanco;
    }

    @Transactional
    public Hemobanco updateHemobanco(Long id, Hemobanco updatedHemobanco) {
        Hemobanco hemobanco = hemobancoRepository.findById(id);
        if (hemobanco == null) {
            throw new NotFoundException("Hemobanco with id " + id + " not found");
        }

        hemobanco.setAddress(updatedHemobanco.getAddress());
        // Atualize outros campos, se necessário

        return hemobanco;
    }

    @Transactional
    public void deleteHemobanco(Long id) {
        hemobancoRepository.deleteById(id);
    }

    @Transactional
    public Hemobanco registerHemobanco(String address) {
        Hemobanco hemobanco = new Hemobanco();
        hemobanco.setAddress(address);
        hemobancoRepository.persist(hemobanco);
        return hemobanco;
    }

    public Optional<Hemobanco> getHemobancoById(Long id) {
        return hemobancoRepository.findByIdOptional(id);
    }
}
