package org.acme.controller;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.HemobancoDateService;
import org.acme.domain.HemobancoDate;
import java.util.List;

@Path("/hemobanco_dates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HemobancoDateController {

    @Inject
    HemobancoDateService dateService;

    @GET
    public List<HemobancoDate> getAllDates() {
        return (List<HemobancoDate>) dateService.getAllDates();
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
            HemobancoDate savedDate = dateService.saveDate(date);
            return Response.ok(savedDate).build();
        } catch (Exception e) {
            String errorMessage = "Erro ao criar a data disponível.";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
    }
    @PUT
    @Path("/{id}")
    public HemobancoDate updateDate(@PathParam("id") Long id, HemobancoDate updatedDate) {
        HemobancoDate date = dateService.getDateById(id)
                .orElseThrow(() -> new NotFoundException("Date with id " + id + " not found"));

        date.setAvailableDates(updatedDate.getAvailableDates());
        return dateService.saveDate(date);
    }

    @DELETE
    @Path("/{id}")
    public void deleteDate(@PathParam("id") Long id) {
        dateService.deleteDate(id);
    }
}
