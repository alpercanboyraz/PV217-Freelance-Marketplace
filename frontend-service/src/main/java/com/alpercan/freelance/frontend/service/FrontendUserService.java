package com.alpercan.freelance.frontend.service;

import com.alpercan.freelance.frontend.client.UserServiceClient;
import com.alpercan.freelance.frontend.dto.ProfileRequest;
import com.alpercan.freelance.frontend.dto.UserResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class FrontendUserService {

    @Inject
    @RestClient
    UserServiceClient userServiceClient;

    public UserResponse getProfile(String token) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;
        return userServiceClient.getProfile(token);
    }

    public void updateProfile(String token, String bio, String pictureUrl, String website, String location) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;

        ProfileRequest request = new ProfileRequest(bio, pictureUrl, website, location);
        userServiceClient.updateProfile(token, request);
    }
}