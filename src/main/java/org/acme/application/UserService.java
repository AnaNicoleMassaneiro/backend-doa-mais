package org.acme.application;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.User;
import org.acme.validators.UserValidator;

import java.util.List;

@ApplicationScoped
public class UserService implements PanacheRepository<User> {

    private final UserValidator userValidator;

    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @Transactional
    public void createUser(User user) {
        if (!userValidator.validateCpf(user.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (!userValidator.validateEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-mail inválido");
        }

        persist(user);
    }

    @Transactional
    public List<User> listUsers() {
        return listAll();
    }

    public void updateUser(User user) {
        User existingUser = findById(user.getId());

        if (!userValidator.validateCpf(user.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (!userValidator.validateEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-mail inválido");
        }

        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setCpf(user.getCpf());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            persist(existingUser);
        }
    }

    public void deleteUser(Long id) {
        User user = findById(id);
        if (user != null) {
            delete(user);
        }
    }

}