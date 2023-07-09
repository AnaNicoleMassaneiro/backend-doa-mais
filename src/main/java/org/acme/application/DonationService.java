package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Donation;
import org.acme.validators.DonationValidator;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DonationService {

    private final DonationValidator donationValidator;

    private List<Donation> scheduledDonations;

    public DonationService(DonationValidator donationValidator) {
        this.donationValidator = donationValidator;
        this.scheduledDonations = new ArrayList<>();
    }

    public void scheduleDonation(Donation donation) {
        // Verificar se a doação já está agendada
        if (donationValidator.isDonationScheduled(donation)) {
            throw new IllegalArgumentException("A doação já está agendada.");
        }

        // Validar os dados da doação (por exemplo, data válida, número de cartão válido, etc.)

        // Realizar a lógica de agendamento da doação
        // Por exemplo, salvar a doação em um banco de dados ou em alguma outra fonte de armazenamento

        // Adicionar a doação à lista de doações agendadas
        scheduledDonations.add(donation);
    }
}