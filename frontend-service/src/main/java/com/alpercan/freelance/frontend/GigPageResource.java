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
    @GET
    @Path("/my")
    @Produces(MediaType.TEXT_HTML)
    public Response showMyGigsPage(@CookieParam("jwt_token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.seeOther(URI.create("/auth/login")).build();
        }

        try {
            String bearerToken = token.startsWith("Bearer ") ? token : "Bearer " + token;
            List<GigResponse> myGigs = gigService.getMyGigs(bearerToken);

            return Response.ok(
                    gigsTemplate.data("gigs", myGigs)
                            .data("allowDelete", true)
                            // --- EKLENEN KISIM BAŞLANGIÇ ---
                            // Şablonun hata vermemesi için boş değerler gönderiyoruz
                            .data("selectedCategory", "")
                            .data("selectedSort", "")
                    // --- EKLENEN KISIM BİTİŞ ---
            ).build();

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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showAllGigs(@QueryParam("category") String category,
                                        @QueryParam("sort") String sort) {

        List<GigResponse> gigs = gigService.getAllGigs(category, sort);

        // Null ise boş string yap ki HTML patlamasın
        String safeCategory = category == null ? "" : category;
        String safeSort = sort == null ? "" : sort;

        return gigsTemplate.data("gigs", gigs)
                .data("allowDelete", false)
                .data("selectedCategory", safeCategory)
                .data("selectedSort", safeSort);
    }
}