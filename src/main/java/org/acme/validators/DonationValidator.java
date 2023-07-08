package org.acme.validators;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Donation;

import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class DonationValidator {

    @Inject
    private List<Donation> scheduledDonations;

    public boolean isDonationScheduled(Donation donation) {
        // Verificar se a doação já está agendada com base em algum critério (por exemplo, ID, data, etc.)
        // Retorna true se a doação já estiver agendada, caso contrário, retorna false
        return scheduledDonations.contains(donation);
    }

    // Outras regras de validação de doação podem ser adicionadas aqui
}