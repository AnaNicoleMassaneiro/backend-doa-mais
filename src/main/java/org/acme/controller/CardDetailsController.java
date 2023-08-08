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
    public Response getCardDetailsByUserId(@PathParam("userId") Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new NotFoundException("User with ID " + userId + " not found"));

        CardDetails cardDetails = cardDetailsRepository.findByUserId(userId);

        if (cardDetails == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(cardDetails).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrUpdateCardDetails(CardDetails cardDetails) {
        return cardDetailsService.createOrUpdateCardDetails(cardDetails);
    }
}
