package com.alpercan.freelance.frontend.service;

import com.alpercan.freelance.frontend.client.PaymentServiceClient;
import com.alpercan.freelance.frontend.dto.CreateOrderRequest;
import com.alpercan.freelance.frontend.dto.GigResponse;
import com.alpercan.freelance.frontend.dto.OrderResponse;
import com.alpercan.freelance.frontend.dto.OrderViewDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class FrontendPaymentService {

    @Inject
    @RestClient
    PaymentServiceClient paymentClient;

    @Inject
    FrontendGigService gigService; // İlan detaylarını sormak için

    public void createOrder(String token, Long gigId) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;
        CreateOrderRequest request = new CreateOrderRequest(gigId);
        paymentClient.createOrder(token, request);
    }

    public void payOrder(String token, Long orderId) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;
        paymentClient.payOrder(token, orderId);
    }

    // Orkestrasyon: Siparişleri + İlan Detaylarını birleştir
    public List<OrderViewDTO> getMyOrders(String token) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;

        try {
            List<OrderResponse> orders = paymentClient.getMyOrders(token);
            List<OrderViewDTO> orderViews = new ArrayList<>();

            for (OrderResponse order : orders) {
                // Her sipariş için Gig detayını çek
                // (Not: Gerçek hayatta bunu 'Batch Get' ile yapmak daha performanslıdır ama MVP için bu OK)
                GigResponse gig = gigService.getAllGigs().stream()
                        .filter(g -> g.id().equals(order.gigId()))
                        .findFirst()
                        .orElse(null);

                orderViews.add(new OrderViewDTO(order, gig));
            }
            return orderViews;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public void cancelOrder(String token, Long orderId) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;
        paymentClient.cancelOrder(token, orderId);
    }
}