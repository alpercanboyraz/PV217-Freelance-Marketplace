package com.alpercan.freelance.gig.resource;

import com.alpercan.freelance.gig.dto.GigRequest;
import com.alpercan.freelance.gig.model.Gig;
import com.alpercan.freelance.gig.service.GigService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.util.List;

@Path("/api/gigs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GigResource {

    @Inject
    GigService gigService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Authenticated
    public Response createGig(GigRequest request) {


        Object userIdClaim = jwt.getClaim("userId");
        if (userIdClaim == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Token: userId missing").build();
        }

        Long sellerId = Long.parseLong(userIdClaim.toString());

        Gig gig = gigService.createGig(request, sellerId);
        return Response.created(URI.create("/api/gigs/" + gig.id)).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Gig gig = gigService.getById(id);
        if (gig == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(gig).build();
    }
    @GET
    @Path("/seller/{sellerId}")
    public List<Gig> getGigsBySeller(@PathParam("sellerId") Long sellerId) {
        return gigService.getGigsBySeller(sellerId);
    }
    @GET
    @Path("/my")
    @Authenticated
    public List<Gig> getMyGigs() {
        Long sellerId = Long.parseLong(jwt.getClaim("userId").toString());
        return gigService.getMyGigs(sellerId);
    }

    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteGig(@PathParam("id") Long id) {

        Long userId = Long.parseLong(jwt.getClaim("userId").toString());

        boolean deleted = gigService.deleteGig(id, userId);

        if (deleted) {
            return Response.noContent().build();
        } else {

            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    @GET
    public List<Gig> list(@QueryParam("category") String category,
                          @QueryParam("sort") String sort) {
        return gigService.search(category, sort);
    }
}