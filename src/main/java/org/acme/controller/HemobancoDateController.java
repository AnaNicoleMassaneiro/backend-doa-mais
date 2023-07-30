package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Hemobanco;
import org.acme.domain.HemobancoDate;
import org.acme.service.HemobancoDateService;
import org.acme.service.HemobancoService;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Path("/hemobanco_dates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HemobancoDateController {

    @Inject
    HemobancoDateService dateService;

    @Inject
    HemobancoService hemobancoService;

    @GET
    public List<HemobancoDate> getAllDates() {
        return dateService.getAllDates();
    }

    @GET
    @Path("/{id}")
    public HemobancoDate getDateById(@PathParam("id") Long id) {
        return dateService.getDateById(id)
                .orElseThrow(() -> new NotFoundException("Date with id " + id + " not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDate(HemobancoDate date) {
        try {
            Long hemobancoAddressId = date.getHemobancoAddressId();

            // Check if the Hemobanco with the provided hemobancoAddressId exists
            Hemobanco hemobanco = hemobancoService.getHemobancoById(hemobancoAddressId)
                    .orElseThrow(() -> new NotFoundException("Hemobanco with id " + hemobancoAddressId + " not found"));


            // Set the retrieved Hemobanco entity as the hemobancoAddress for the HemobancoDate
            date.setHemobancoAddress(hemobanco);

            HemobancoDate savedDate = dateService.saveDate(date);
            return Response.ok(savedDate).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            String errorMessage = "Erro ao criar a data disponível.";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
    }

}
