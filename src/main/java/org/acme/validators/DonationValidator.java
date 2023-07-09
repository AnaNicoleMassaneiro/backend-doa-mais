package org.acme.validators;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Donation;

@ApplicationScoped
public class DonationValidator {

    public boolean isDonationScheduled(Donation donation) {
        // Verificar se a doação já está agendada com base em algum critério (por exemplo, ID, data, etc.)
        // Retorna true se a doação já estiver agendada, caso contrário, retorna false
        return false;
    }

    // Outras regras de validação de doação podem ser adicionadas aqui
}