package com.alpercan.freelance.user.resource;

import com.alpercan.freelance.user.dto.LoginRequest;
import com.alpercan.freelance.user.dto.ProfileRequest;
import com.alpercan.freelance.user.dto.RegisterRequest;
import com.alpercan.freelance.user.model.User;
import com.alpercan.freelance.user.service.UserService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import io.smallrye.common.annotation.Blocking;
import jakarta.transaction.Transactional;
import java.net.URI;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

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
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(LoginRequest request) {
        try {
            String token = userService.login(request);
            return Response.ok(token).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/profile")
    @Authenticated
    @Blocking // EKLE: Veritabanına gittiği için bloklayabilir
    public Response getProfile() {
        String email = jwt.getName();
        User user = userService.getProfile(email);
        return Response.ok(user).build();
    }

    @PUT
    @Path("/profile")
    @Authenticated
    @Transactional
    @Blocking // EKLE: Veritabanına yazma yaptığı için bloklayabilir
    public Response updateProfile(ProfileRequest request) {
        String email = jwt.getName();
        User user = userService.updateProfile(email, request);
        return Response.ok(user).build();
    }
}

//Email: alper@test.com
//
//Password: superSecretPassword123