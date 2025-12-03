package com.alpercan.freelance.payment.resource;

import com.alpercan.freelance.payment.dto.CreateOrderRequest;
import com.alpercan.freelance.payment.model.Order;
import com.alpercan.freelance.payment.service.PaymentService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.transaction.Transactional;
import java.util.List;

@Path("/api/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class PaymentResource {

    @Inject
    PaymentService paymentService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/orders")
    public Response createOrder(CreateOrderRequest request) {
        Long buyerId = Long.parseLong(jwt.getClaim("userId").toString());
        Order order = paymentService.createOrder(buyerId, request);
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @PUT
    @Path("/orders/{id}/pay")
    public Response payOrder(@PathParam("id") Long id) {

        try {
            Order order = paymentService.completePayment(id);
            return Response.ok(order).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/orders")
    public List<Order> getMyOrders() {
        Long userId = Long.parseLong(jwt.getClaim("userId").toString());
        return paymentService.getMyOrders(userId);
    }

    @DELETE
    @Path("/orders/{id}")
    @Transactional
    public Response cancelOrder(@PathParam("id") Long id) {

        boolean deleted = Order.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}