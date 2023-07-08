package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.User;

@ApplicationScoped
public class UserService {
    @Transactional
    public void registerUser(User user) {
        // Verifique a validade dos dados do usuário, faça validações, etc.

        // Exemplo de validação de campos obrigatórios
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do usuário é obrigatório.");
        }

        // Outras validações...

        // Registre o usuário no banco de dados usando o Panache do Quarkus
        user.persist();
    }
}