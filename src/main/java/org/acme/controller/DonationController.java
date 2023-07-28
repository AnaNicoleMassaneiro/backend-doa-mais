package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.DonationService;
import org.acme.domain.Donation;


@Path("/donations")
public class DonationController {
    private final DonationService donationService;

    @Inject
    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response scheduleDonation(Donation donation) {
        donationService.scheduleDonation(donation);
        return Response.ok().build();
    }

}