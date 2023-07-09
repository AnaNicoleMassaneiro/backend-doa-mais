package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.acme.domain.User;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepository {

    private List<User> users = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepository() {

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
}

