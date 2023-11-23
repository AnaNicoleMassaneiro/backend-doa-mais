package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.CardDetails;
import org.acme.domain.User;
import org.acme.repository.CardDetailsRepository;
import org.acme.repository.UserRepository;
import org.acme.service.CardDetailsService;

import java.time.LocalDate;

@Path("/cardDetails")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardDetailsController {
    @Inject
    CardDetailsService cardDetailsService;

    @Inject
    UserRepository userRepository;

    @Inject
    CardDetailsRepository cardDetailsRepository;

    @GET
    @Path("/{userId}")
    @Transactional // Add this annotation
    public Response getCardDetailsByUserId(@PathParam("userId") Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        CardDetails cardDetails = cardDetailsRepository.findByUserId(userId);

        if (cardDetails == null) {
            // If cardDetails does not exist, create a new one with user information
            cardDetails = new CardDetails();
            cardDetails.setCardNumber("00");  // Set a default card number or provide default values
            cardDetails.setValidity(LocalDate.now().plusYears(1));  // Set a default validity or provide default values
            cardDetails.setUser(user);
            cardDetails.setBloodType("0+");
            cardDetailsRepository.persist(cardDetails);
        }

        return Response.ok(cardDetails).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createOrUpdateCardDetails(CardDetails cardDetails) {
        return cardDetailsService.createOrUpdateCardDetails(cardDetails);
    }
}
