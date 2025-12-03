package com.alpercan.freelance.frontend.client;

import com.alpercan.freelance.frontend.dto.LoginRequest;
import com.alpercan.freelance.frontend.dto.ProfileRequest;
import com.alpercan.freelance.frontend.dto.RegisterRequest;
import com.alpercan.freelance.frontend.dto.UserResponse;
import jakarta.ws.rs.*; // Tüm JAX-RS anotasyonlarını (GET, POST, PathParam vs.) kapsar
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api/users")
@RegisterRestClient(configKey = "user-api")
public interface UserServiceClient {

    @POST
    @Path("/login")
    String login(LoginRequest request);

    @POST
    @Path("/register")
    void register(RegisterRequest request);

    @GET
    @Path("/profile")
    UserResponse getProfile(@HeaderParam("Authorization") String token);

    @PUT
    @Path("/profile")
    UserResponse updateProfile(@HeaderParam("Authorization") String token, ProfileRequest request);

    // --- EKSİK OLAN KISIM BURASIYDI ---
    @GET
    @Path("/{id}")
    UserResponse getUserById(@PathParam("id") Long id);
}