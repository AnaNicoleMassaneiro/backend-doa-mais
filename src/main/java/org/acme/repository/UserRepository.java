package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.User;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepository {

    private List<User> users = new ArrayList<>();

    public UserRepository() {

    }

    public User findByUsername(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}

