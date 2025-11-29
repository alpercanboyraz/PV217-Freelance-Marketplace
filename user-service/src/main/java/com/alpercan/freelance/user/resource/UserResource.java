package com.alpercan.freelance.user.resource;

import com.alpercan.freelance.user.dto.LoginRequest;      // EKLENDİ
import com.alpercan.freelance.user.dto.RegisterRequest;
import com.alpercan.freelance.user.model.User;
import com.alpercan.freelance.user.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            User user = userService.register(request);
            return Response.created(URI.create("/api/users/" + user.id)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            String token = userService.login(request);
            return Response.ok(token).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}