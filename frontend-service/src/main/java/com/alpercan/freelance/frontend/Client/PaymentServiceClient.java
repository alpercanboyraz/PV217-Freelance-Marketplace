package com.alpercan.freelance.frontend.client;

import com.alpercan.freelance.frontend.dto.CreateOrderRequest;
import com.alpercan.freelance.frontend.dto.OrderResponse;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import jakarta.ws.rs.DELETE;
@Path("/api/payment")
@RegisterRestClient(configKey = "payment-api")
public interface PaymentServiceClient {

    @POST
    @Path("/orders")
    OrderResponse createOrder(@HeaderParam("Authorization") String token, CreateOrderRequest request);

    @PUT
    @Path("/orders/{id}/pay")
    OrderResponse payOrder(@HeaderParam("Authorization") String token, @PathParam("id") Long id);

    @GET
    @Path("/orders")
    List<OrderResponse> getMyOrders(@HeaderParam("Authorization") String token);

    @DELETE
    @Path("/orders/{id}")
    void cancelOrder(@HeaderParam("Authorization") String token, @PathParam("id") Long id);
}