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

        // Verifique se todos os dígitos são iguais
        boolean allDigitsEqual = true;
        for (int i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                allDigitsEqual = false;
                break;
            }
        }
        if (allDigitsEqual) {
            return false;
        }

        // Verifique os dígitos verificadores
        int[] weights = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * weights[i];
        }
        int remainder = sum % 11;
        int digit1 = remainder < 2 ? 0 : 11 - remainder;

        if ((cpf.charAt(9) - '0') != digit1) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * weights[i];
        }
        remainder = sum % 11;
        int digit2 = remainder < 2 ? 0 : 11 - remainder;

        return (cpf.charAt(10) - '0') == digit2;
    }


    public boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        return email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+");
    }

}