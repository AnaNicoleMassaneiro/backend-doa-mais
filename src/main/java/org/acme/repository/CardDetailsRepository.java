package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;
import org.acme.domain.CardDetails;


import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.User;

import java.util.Optional;

@ApplicationScoped
public class CardDetailsRepository implements PanacheRepository<CardDetails> {

    @Inject
    EntityManager entityManager;

    public CardDetails findByUserId(Long userId) {
        try {
            return entityManager.createQuery(
                            "SELECT c FROM CardDetails c WHERE c.user.id = :userId", CardDetails.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }


}
