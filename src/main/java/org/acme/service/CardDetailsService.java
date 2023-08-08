package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.acme.domain.CardDetails;
import org.acme.domain.User;
import org.acme.repository.CardDetailsRepository;
import org.acme.repository.UserRepository;

@ApplicationScoped
public class CardDetailsService implements PanacheRepository<CardDetails> {
    @Inject
    UserRepository userRepository;

    @Inject
    CardDetailsRepository cardDetailsRepository;

    @Transactional
    public Response createOrUpdateCardDetails(CardDetails cardDetails) {
        Long userId = cardDetails.getUser().getId();

        try {
            User user = userRepository.findByIdOptional(userId)
                    .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

            CardDetails existingCardDetails = cardDetailsRepository.findByUserId(userId);
            if (existingCardDetails != null) {
                // Update the existingCardDetails with the new data
                existingCardDetails.setCardNumber(cardDetails.getCardNumber());
                existingCardDetails.setValidity(cardDetails.getValidity());
                existingCardDetails.setBloodType(cardDetails.getBloodType());
                // No need to call persist, as the existingCardDetails is already managed
            } else {
                User managedUser = getEntityManager().getReference(User.class, userId);
                cardDetails.setUser(managedUser);
                persist(cardDetails);
            }

            return Response.ok(cardDetails).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request.").build();
        }
    }
}
