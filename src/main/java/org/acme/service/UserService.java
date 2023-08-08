package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTO.LoginDTO;
import org.acme.domain.User;
import org.acme.exception.AuthenticationException;
import org.acme.repository.CardDetailsRepository;
import org.acme.repository.UserRepository;
import org.acme.validators.UserValidator;

import java.util.List;

@ApplicationScoped
public class UserService implements PanacheRepository<User> {

    @Inject
    UserRepository userRepository;

    @Inject
    CardDetailsRepository cardDetailsRepository;

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

    public User login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            return user;
        }

        throw new AuthenticationException("Invalid username or password");
    }

}