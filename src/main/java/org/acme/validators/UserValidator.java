package org.acme.validators;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class UserValidator {

    public boolean validateCpf(String cpf) {
        // Lógica de validação do CPF
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifique se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        return true;
    }


    public boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        if (!email.contains("@")) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            return false;
        }

        if (!parts[1].contains(".")) {
            return false;
        }

        if (parts[1].indexOf('.') <= 0) {
            return false;
        }

        if (parts[1].length() - parts[1].lastIndexOf('.') <= 1) {
            return false;
        }

        return true;
    }


}