package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.AppointmentRequestDTO;
import org.acme.domain.Appointment;
import org.acme.exception.UserHasAppointmentsException;
import org.acme.service.AppointmentService;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
            throw new UserHasAppointmentsException("User with ID " + e.getMessage());
        }
    }

    @DELETE
    @Path("/{userId}")
    public Response cancelAppointment(@PathParam("userId") Long userId) {
        try {
            appointmentService.cancelAppointmentByUserId(userId);
            return Response.ok().build(); // Return 200 OK if cancellation is successful
        } catch (UserHasAppointmentsException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Appointment with ID " + userId + " not found.")
                    .build();
        } catch (Exception e) {
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

    @GET
    @Path("/user/{userId}/next-donation")
    public Response calculateNextDonationTime(@PathParam("userId") Long userId) {
        try {
            Appointment nextAppointment = appointmentService.getNextAppointmentForUser(userId);

            if (nextAppointment == null) {
                JsonObject responseJson = Json.createObjectBuilder()
                        .add("message", "No upcoming appointments found for the user.")
                        .build();
                return Response.ok(responseJson).build();
            }

            LocalDate appointmentDate = nextAppointment.getDate();
            LocalTime appointmentTime = nextAppointment.getTime();

            LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);

            LocalDateTime currentDateTime = LocalDateTime.now();

            long timeRemainingDays = currentDateTime.until(appointmentDateTime, ChronoUnit.DAYS);

            if (timeRemainingDays <= 0) {
                JsonObject responseJson = Json.createObjectBuilder()
                        .add("message", "The next appointment has already passed.")
                        .build();
                return Response.ok(responseJson).build();
            }

            JsonObject responseJson = Json.createObjectBuilder()
                    .add("timeRemainingDays", timeRemainingDays)
                    .build();

            return Response.ok(responseJson).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
