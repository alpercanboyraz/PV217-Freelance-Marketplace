package com.alpercan.freelance.frontend;

import com.alpercan.freelance.frontend.dto.OrderViewDTO;
import com.alpercan.freelance.frontend.service.FrontendPaymentService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/payment")
public class PaymentPageResource {

    @Inject
    @Location("orders.html")
    Template ordersTemplate;

    @Inject
    FrontendPaymentService paymentService;

    // Sipariş Oluştur (Buy Now butonu buraya gelecek)
    @POST
    @Path("/checkout/{gigId}")
    public Response createOrder(@CookieParam("jwt_token") String token, @PathParam("gigId") Long gigId) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        try {
            paymentService.createOrder(token, gigId);
            return Response.seeOther(URI.create("/payment/orders")).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/?error=true")).build();
        }
    }

    // Ödeme Yap (Pay Now butonu)
    @POST
    @Path("/pay/{orderId}")
    public Response payOrder(@CookieParam("jwt_token") String token, @PathParam("orderId") Long orderId) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        try {
            paymentService.payOrder(token, orderId);
            return Response.seeOther(URI.create("/payment/orders")).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/payment/orders?error=true")).build();
        }
    }

    // Siparişlerim Sayfası
    @GET
    @Path("/orders")
    @Produces(MediaType.TEXT_HTML)
    public Response showOrders(@CookieParam("jwt_token") String token) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        List<OrderViewDTO> myOrders = paymentService.getMyOrders(token);
        return Response.ok(ordersTemplate.data("orders", myOrders)).build();
    }

    @POST
    @Path("/cancel/{orderId}")
    public Response cancelOrder(@CookieParam("jwt_token") String token, @PathParam("orderId") Long orderId) {
        if (token == null) return Response.seeOther(URI.create("/auth/login")).build();

        try {
            paymentService.cancelOrder(token, orderId);
            return Response.seeOther(URI.create("/payment/orders")).build();
        } catch (Exception e) {
            return Response.seeOther(URI.create("/payment/orders?error=true")).build();
        }
    }
}