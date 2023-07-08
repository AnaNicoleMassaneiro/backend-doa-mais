package org.acme.application;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.User;

@ApplicationScoped
public class UserService implements PanacheRepository<User> {
    @Transactional
    public void registerUser(User user) {

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do usuário é obrigatório.");
        }

        persist(user);
    }
}