package com.alpercan.freelance.frontend;

import com.alpercan.freelance.frontend.dto.UserResponse;
import com.alpercan.freelance.frontend.service.FrontendUserService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/profile")
public class UserPageResource {

    @Inject
    @Location("profile.html")
    Template profileTemplate;

    @Inject
    FrontendUserService userService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response showProfilePage(@CookieParam("jwt_token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        try {
            // Mevcut bilgileri çekip sayfaya gönderiyoruz (Pre-fill)
            UserResponse user = userService.getProfile(token);
            return Response.ok(profileTemplate.data("user", user)).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateProfile(@CookieParam("jwt_token") String token,
                                  @FormParam("bio") String bio,
                                  @FormParam("pictureUrl") String pictureUrl,
                                  @FormParam("website") String website,
                                  @FormParam("location") String location) {

        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        try {
            userService.updateProfile(token, bio, pictureUrl, website, location);
            // Güncelleme bitince sayfayı yenile (Veriler güncel gelsin)
            return Response.seeOther(URI.create("/profile")).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.seeOther(URI.create("/profile?error=true")).build();
        }
    }
}