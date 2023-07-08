package org.acme.application;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.User;
import org.acme.validators.UserValidator;

@ApplicationScoped
public class UserService implements PanacheRepository<User> {

    private final UserValidator userValidator;

    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    public void createUser(User user) {
        if (!userValidator.validateCpf(user.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (!userValidator.validateEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-mail inválido");
        }

        persist(user);
    }

}