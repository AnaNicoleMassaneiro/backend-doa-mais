package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Hemobanco;
import org.acme.repository.HemobancoRepository;

@ApplicationScoped
public class HemobancoService {

    @Inject
    HemobancoRepository hemobancoRepository;

    @Transactional
    public Hemobanco registerHemobanco(String address) {
        Hemobanco hemobanco = new Hemobanco();
        hemobanco.setAddress(address);
        hemobancoRepository.persist(hemobanco);
        return hemobanco;
    }

}