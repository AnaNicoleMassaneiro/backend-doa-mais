package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.AppointmentRequestDTO;
import org.acme.domain.Appointment;
import org.acme.service.AppointmentService;

import java.net.URI;
import java.util.List;

@Path("/appointments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AppointmentController {

    @Inject
    AppointmentService appointmentService;

    @POST
    @Transactional
    public Response scheduleAppointment(AppointmentRequestDTO requestDTO) {
        try {
            Appointment appointment = new Appointment();
            appointment.setHemobancoId(requestDTO.getHemobancoId());
            appointment.setUserId(requestDTO.getUserId());
            appointment.setDate(requestDTO.getDate());
            appointment.setTime(requestDTO.getTime());

            Appointment scheduledAppointment = appointmentService.scheduleAppointment(appointment);

            // Return the entire appointment object and status 201 Created
            return Response.created(URI.create("/appointments/" + scheduledAppointment.getId()))
                    .entity(scheduledAppointment)
                    .build();
        } catch (Exception e) {
            // In case of an error, return status 500 Internal Server Error
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response getAppointmentsByUser(@PathParam("userId") Long userId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByUser(userId);
            return Response.ok(appointments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
