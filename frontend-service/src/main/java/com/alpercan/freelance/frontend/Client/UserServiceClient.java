package com.alpercan.freelance.frontend.client;

import com.alpercan.freelance.frontend.dto.LoginRequest;
import com.alpercan.freelance.frontend.dto.ProfileRequest; // EKLENDİ
import com.alpercan.freelance.frontend.dto.RegisterRequest; // Bu da lazım olabilir
import com.alpercan.freelance.frontend.dto.UserResponse;   // EKLENDİ
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;                                  // EKLENDİ
import jakarta.ws.rs.Path;
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
}