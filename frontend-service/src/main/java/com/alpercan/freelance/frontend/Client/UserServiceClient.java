package com.alpercan.freelance.frontend.client;

import com.alpercan.freelance.frontend.dto.LoginRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import com.alpercan.freelance.frontend.dto.UserResponse;
import com.alpercan.freelance.frontend.dto.RegisterRequest;
@Path("/api/users")
@RegisterRestClient(configKey = "user-api")
public interface UserServiceClient {

    @POST
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    String login(LoginRequest request);

    @GET
    @Path("/profile")
    UserResponse getProfile(@HeaderParam("Authorization") String token);

    @POST
    @Path("/register")
    void register(RegisterRequest request);

}