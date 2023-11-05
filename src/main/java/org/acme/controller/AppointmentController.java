package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTO.AppointmentRequestDTO;
import org.acme.domain.Appointment;
import org.acme.repository.AppointmentRepository;
import org.acme.service.PdfService;
import org.acme.exception.UserHasAppointmentsException;
import org.acme.service.AppointmentService;

import java.io.IOException;
import java.io.InputStream;
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

    @Inject
    UserTransaction userTransaction;

    @Inject
    PdfService pdfService;

    @Inject
    private AppointmentRepository appointmentRepository;

    @POST
    @Transactional
    public Response scheduleAppointment(AppointmentRequestDTO requestDTO) {
        try {
            Appointment appointment = new Appointment();
            appointment.setHemobancoId(requestDTO.getHemobancoId());
            appointment.setUserId(requestDTO.getUserId());
            appointment.setDate(requestDTO.getDate());
            appointment.setTime(requestDTO.getTime());
            appointment.setCompleted(false);

            Appointment scheduledAppointment = appointmentService.scheduleAppointment(appointment);

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
            List<Appointment> appointments = appointmentService.getAppointmentsWithHemobancoByUser(userId);
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

    @POST
    @Path("/upload-pdf")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPdf(
            InputStream pdfFileInputStream,
            @QueryParam("appointmentId") Long appointmentId
    ) {
        try {
            Appointment appointment = appointmentService.findAppointmentById(appointmentId);

            if (appointment == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Agendamento não encontrado").build();
            }

            pdfService.savePdfForAppointment(pdfFileInputStream, appointment);

            return Response.ok("PDF recebido e associado ao agendamento com sucesso").build();

        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao processar o arquivo PDF").build();
        }
    }

    @GET
    @Path("/{appointmentId}/download-pdf")
    @Produces("application/pdf")
    public Response downloadPdfForAppointment(@PathParam("appointmentId") Long appointmentId) {
        // Verifique se existe um PDF associado ao agendamento
        boolean hasPdf = pdfService.hasPdfForAppointment(appointmentId);

        if (!hasPdf) {
            return Response.status(Response.Status.NOT_FOUND).entity("PDF não encontrado").build();
        }

        try {
            // Inicie a transação
            userTransaction.begin();

            // Recupere o PDF associado ao agendamento como um InputStream
            InputStream pdfStream = pdfService.downloadPdfForAppointment(appointmentId);

            // Configure a resposta para o download do PDF
            Response.ResponseBuilder response = Response.ok(pdfStream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=appointment_" + appointmentId + ".pdf");

            // Finalize a transação
            userTransaction.commit();

            return response.build();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return Response.serverError().entity("Erro ao baixar o PDF").build();
        }
    }
    @GET
    public List<Appointment> getAllAppointmentSlots() {
        return appointmentService.getAllAppointment();
    }

    @PUT
    @Path("/{appointmentId}/complete")
    @Transactional
    public Response markAppointmentAsCompleted(@PathParam("appointmentId") Long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId);
            if (appointment != null) {
                appointment.setCompleted(true); // Marque como "true" quando a doação for realizada
                appointmentService.markAppointmentAsCompleted(appointment);
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            // Em caso de erro, retorne status 500 Internal Server Error
            throw new UserHasAppointmentsException("Error marking appointment as completed: " + e.getMessage());
        }
    }

    @GET
    @Path("/appointment/{appointmentId}/has-pdf")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hasPdfForAppointment(@PathParam("appointmentId") Long appointmentId) {
        boolean hasPdf = pdfService.hasPdfForAppointment(appointmentId);
        return Response.ok(hasPdf).build();
    }
}
