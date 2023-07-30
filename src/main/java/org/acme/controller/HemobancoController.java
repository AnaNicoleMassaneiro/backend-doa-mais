package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Hemobanco;
import org.acme.service.HemobancoService;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@Path("/hemobancos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HemobancoController {

    @Inject
    HemobancoService hemobancoService;

    @GET
    public List<Hemobanco> getAllHemobancos() {
        return hemobancoService.getAllHemobancos();
    }

    @GET
    @Path("/{id}")
    public Hemobanco getHemobancoById(@PathParam("id") Long id) {
        Optional<Hemobanco> optionalHemobanco = hemobancoService.getHemobancoById(id);
        return optionalHemobanco.orElseThrow(() -> new NotFoundException("Hemobanco with id " + id + " not found"));
    }


    @POST
    public Response createHemobanco(Hemobanco hemobanco) {
        try {
            Hemobanco savedHemobanco = hemobancoService.saveHemobanco(hemobanco);
            return Response.ok(savedHemobanco).build();
        } catch (Exception e) {
            String errorMessage = "Erro ao criar o hemobanco.";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorMessage)
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Hemobanco updateHemobanco(@PathParam("id") Long id, Hemobanco updatedHemobanco) {
        Optional<Hemobanco> optionalHemobanco = hemobancoService.getHemobancoById(id);
        Hemobanco hemobanco = optionalHemobanco.orElseThrow(() -> new NotFoundException("Hemobanco with id " + id + " not found"));

        hemobanco.setAddress(updatedHemobanco.getAddress());
        hemobanco.setCity(updatedHemobanco.getCity());
        hemobanco.setState(updatedHemobanco.getState());
        hemobanco.setZipCode(updatedHemobanco.getZipCode());

        return hemobancoService.saveHemobanco(hemobanco);
    }

    @DELETE
    @Path("/{id}")
    public void deleteHemobanco(@PathParam("id") Long id) {
        hemobancoService.deleteHemobanco(id);
    }
}
