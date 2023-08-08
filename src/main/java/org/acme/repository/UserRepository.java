package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.acme.domain.CardDetails;
import org.acme.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    private List<User> users = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepository() {
    }

    public Optional<User> findByIdOptional(Long id) {
        return find("id", id).singleResultOptional();
    }

    @Transactional
    public User findByEmail(String email) {
        try {
            String query = "SELECT u FROM User u WHERE u.email = :email";
            return entityManager.createQuery(query, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Optional<CardDetails> findCardDetailsByUser(User user) {
        String query = "SELECT c FROM CardDetails c WHERE c.user = :user";
        return entityManager.createQuery(query, CardDetails.class)
                .setParameter("user", user)
                .getResultList()
                .stream()
                .findFirst();
    }

}

