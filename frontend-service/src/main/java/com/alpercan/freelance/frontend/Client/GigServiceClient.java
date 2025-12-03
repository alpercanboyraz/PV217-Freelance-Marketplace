package com.alpercan.freelance.frontend.client;

import com.alpercan.freelance.frontend.dto.GigRequest;
import com.alpercan.freelance.frontend.dto.GigResponse;
import jakarta.ws.rs.DELETE;      // EKLENDİ
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;   // İŞTE EKSİK OLAN BUYDU!
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api/gigs")
@RegisterRestClient(configKey = "gig-api")
public interface GigServiceClient {

    @POST
    void createGig(@HeaderParam("Authorization") String token, GigRequest request);

    @GET
    List<GigResponse> getAllGigs();

    @GET
    @Path("/my")
    List<GigResponse> getMyGigs(@HeaderParam("Authorization") String token);

    @DELETE
    @Path("/{id}")
    void deleteGig(@HeaderParam("Authorization") String token, @PathParam("id") Long id);
}