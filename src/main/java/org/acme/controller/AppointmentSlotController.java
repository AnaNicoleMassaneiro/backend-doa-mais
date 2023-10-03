package org.acme.controller;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.AppointmentSlotService;
import org.acme.domain.AppointmentSlot;

@Path("/appointmentSlots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentSlotController {

    @Inject
    private AppointmentSlotService appointmentSlotService;

    @POST
    public Response createAppointmentSlot(AppointmentSlot appointmentSlot) {
        try {
            appointmentSlotService.createAppointmentSlot(appointmentSlot);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar o slot de agendamento: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getAllAppointmentSlots() {
        return Response.ok(appointmentSlotService.getAllAppointmentSlots()).build();
    }
}
