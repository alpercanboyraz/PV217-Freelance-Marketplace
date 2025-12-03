package com.alpercan.freelance.frontend;

import com.alpercan.freelance.frontend.client.UserServiceClient;
import com.alpercan.freelance.frontend.dto.GigResponse; // EKLENDİ
import com.alpercan.freelance.frontend.dto.UserResponse;
import com.alpercan.freelance.frontend.service.FrontendGigService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List; // EKLENDİ

@Path("/")
public class HomeResource {

    @Inject
    Template home;

    @Inject
    @RestClient
    UserServiceClient userService;

    @Inject
    FrontendGigService gigService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@CookieParam("jwt_token") String token) {
        String name = "Guest";
        boolean isLoggedIn = false;
        boolean hasGigs = false;

        if (token != null && !token.isEmpty()) {
            try {
                String bearerToken = token.startsWith("Bearer ") ? token : "Bearer " + token;
                UserResponse user = userService.getProfile(bearerToken);

                name = user.fullName();
                isLoggedIn = true;

                hasGigs = gigService.hasGigs(token);

            } catch (Exception e) {
                System.out.println("Session check failed: " + e.getMessage());
            }
        }

        // Öne çıkan ilanları çek
        List<GigResponse> featuredGigs = gigService.getFeaturedGigs();

        return home.data("name", name)
                .data("isLoggedIn", isLoggedIn)
                .data("hasGigs", hasGigs)
                .data("featuredGigs", featuredGigs);
    }
}