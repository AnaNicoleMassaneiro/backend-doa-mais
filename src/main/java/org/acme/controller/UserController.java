package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.repository.UserRepository;
import org.acme.service.CardDetailsService;
import org.acme.service.UserService;
import org.acme.domain.User;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserService userService;

    @Inject
    CardDetailsService cardDetailsService;

    @Inject
    UserRepository userRepository;
    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @POST
    public Response registerUser(User user) {
        userService.createUser(user);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return userService.listUsers();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, User updatedUser) {
        updatedUser.setId(id);
        userService.updateUser(updatedUser);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") Long id) {
        return userService.getUser(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }
}