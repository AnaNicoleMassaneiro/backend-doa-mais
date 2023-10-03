package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Request.AvailableDateRequest;
import org.acme.Request.AvailableTimeSlotRequest;
import org.acme.Request.CreateDateRequest;
import org.acme.domain.AvailableDateEntity;
import org.acme.domain.AvailableTimeSlot;
import org.acme.domain.Hemobanco;
import org.acme.domain.HemobancoDate;
import org.acme.service.HemobancoDateService;
import org.acme.service.HemobancoService;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public Response getById(@PathParam("id") Long id) {
        try {
            Optional<HemobancoDate> hemobancoDate = dateService.getById(id);

            if (hemobancoDate.isPresent()) {
                return Response.ok(hemobancoDate.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("HemobancoDate with id " + id + " not found").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while fetching HemobancoDate by ID").build();
        }
    }

    @GET
    @Path("/hemobanco/{hemobancoAddressId}")
    public Response getByHemobancoAddressId(@PathParam("hemobancoAddressId") Long hemobancoAddressId) {
        try {
            List<HemobancoDate> hemobancoDates = dateService.getByHemobancoAddressId(hemobancoAddressId);

            if (!hemobancoDates.isEmpty()) {
                return Response.ok(hemobancoDates).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No HemobancoDates found for hemobancoAddressId " + hemobancoAddressId).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while fetching HemobancoDates by hemobancoAddressId").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDate(CreateDateRequest request) {
        try {
            Long hemobancoAddressId = request.getHemobancoAddressId();

            Hemobanco hemobanco = hemobancoService.getHemobancoById(hemobancoAddressId)
                    .orElseThrow(() -> new NotFoundException("Hemobanco with id " + hemobancoAddressId + " not found"));

            List<AvailableDateRequest> availableDateRequests = request.getAvailableDates();
            List<AvailableDateEntity> availableDates = new ArrayList<>();

            for (AvailableDateRequest dateRequest : availableDateRequests) {
                AvailableDateEntity availableDateEntity = new AvailableDateEntity();

                availableDateEntity.setDate(dateRequest.getDate());

                List<AvailableTimeSlot> availableTimeSlots = new ArrayList<>();

                for (AvailableTimeSlotRequest timeSlotRequest : dateRequest.getAvailableTimeSlots()) {
                    AvailableTimeSlot timeSlotEntity = new AvailableTimeSlot();
                    timeSlotEntity.setTime(timeSlotRequest.getTime());

                    availableTimeSlots.add(timeSlotEntity);
                }

                availableDateEntity.setAvailableTimeSlots(availableTimeSlots);

                availableDates.add(availableDateEntity);
            }

            HemobancoDate date = new HemobancoDate();
            date.setHemobancoAddress(hemobanco);
            date.setAvailableDates(availableDates);
            date.setHemobancoAddressId(hemobancoAddressId);

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
