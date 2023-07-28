package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.HemobancoDTO;
import org.acme.service.HemobancoService;
import org.acme.domain.Hemobanco;
import org.acme.repository.HemobancoRepository;

import java.util.List;

@Path("/hemobancos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HemobancoController {

    @Inject
    HemobancoService hemobancoService;

    @Inject
    HemobancoRepository hemobancoRepository;

    @POST
    public Response registerHemobanco(HemobancoDTO hemobancoDTO) {
        Hemobanco hemobanco = hemobancoService.registerHemobanco(hemobancoDTO.getAddress());
        return Response.status(Response.Status.CREATED).entity(hemobanco).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Hemobanco> listarHemobancos() {
        return hemobancoRepository.listAll();
    }
}
