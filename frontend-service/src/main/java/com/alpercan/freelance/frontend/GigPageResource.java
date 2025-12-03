package com.alpercan.freelance.frontend;

import com.alpercan.freelance.frontend.dto.GigResponse;
import com.alpercan.freelance.frontend.service.FrontendGigService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Path("/gigs")
public class GigPageResource {

    @Inject
    @Location("create-gig.html")
    Template createGigTemplate;

    @Inject
    @Location("gigs.html")
    Template gigsTemplate;

    @Inject
    FrontendGigService gigService;

    // --- TÜM İLANLARI LİSTELE ---
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showAllGigs() {
        List<GigResponse> gigs = gigService.getAllGigs();
        // Ana listede silme butonu OLMASIN
        return gigsTemplate.data("gigs", gigs).data("allowDelete", false);
    }

    // --- İLANLARIM SAYFASI ---
    @GET
    @Path("/my")
    @Produces(MediaType.TEXT_HTML)
    public Response showMyGigsPage(@CookieParam("jwt_token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        try {
            // "My Gigs" sayfasında silme butonu OLSUN
            List<GigResponse> myGigs = gigService.getMyGigs(token);
            return Response.ok(gigsTemplate.data("gigs", myGigs).data("allowDelete", true)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.seeOther(URI.create("/?error=true")).build();
        }
    }

    // --- SİLME İŞLEMİ ---
    @POST
    @Path("/delete/{id}")
    public Response deleteGig(@CookieParam("jwt_token") String token, @PathParam("id") Long id) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        try {
            gigService.deleteGig(token, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.seeOther(URI.create("/gigs/my")).build();
    }

    // --- OLUŞTURMA SAYFASI ---
    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_HTML)
    public Response showCreatePage(@CookieParam("jwt_token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }
        return Response.ok(createGigTemplate.instance()).build();
    }

    // --- OLUŞTURMA İŞLEMİ ---
    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response performCreate(@CookieParam("jwt_token") String token,
                                  @FormParam("title") String title,
                                  @FormParam("description") String description,
                                  @FormParam("price") BigDecimal price,
                                  @FormParam("category") String category,
                                  @FormParam("pictureFile") FileUpload pictureFile) {

        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        try {
            gigService.createGig(token, title, description, price, category, pictureFile);
            return Response.seeOther(URI.create("/gigs")).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.seeOther(URI.create("/gigs/create?error=true")).build();
        }
    }
}