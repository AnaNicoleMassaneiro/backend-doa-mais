package org.acme.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.HemobancoDTO;
import org.acme.application.HemobancoService;
import org.acme.domain.Hemobanco;

@Path("/hemobancos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HemobancoController {

    @Inject
    HemobancoService hemobancoService;

    @POST
    public Response registerHemobanco(HemobancoDTO hemobancoDTO) {
        Hemobanco hemobanco = hemobancoService.registerHemobanco(hemobancoDTO.getAddress());
        return Response.status(Response.Status.CREATED).entity(hemobanco).build();
    }
}
