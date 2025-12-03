package com.alpercan.freelance.payment.service;

import com.alpercan.freelance.payment.dto.CreateOrderRequest;
import com.alpercan.freelance.payment.model.Order;
import com.alpercan.freelance.payment.model.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

@ApplicationScoped
public class PaymentService {

    // --- İŞTE EKSİK OLAN KISIM BURASI ---
    @Inject
    @Channel("orders-out")
    Emitter<String> orderEmitter;
    // ------------------------------------

    @Transactional
    public Order createOrder(Long buyerId, CreateOrderRequest request) {
        Order order = new Order();
        order.buyerId = buyerId;
        order.gigId = request.gigId();
        order.persist();
        return order;
    }

    @Transactional
    public Order completePayment(Long orderId) {
        System.out.println("DEBUG: completePayment metodu çağrıldı! Sipariş ID: " + orderId);

        Order order = Order.findById(orderId);
        if (order == null) {
            System.out.println("DEBUG: Sipariş bulunamadı!");
            throw new IllegalArgumentException("Order not found");
        }

        order.status = OrderStatus.COMPLETED;

        // --- KAFKA MESAJI GÖNDERME ---
        String message = "Order #" + order.id + " COMPLETED for Buyer: " + order.buyerId;
        try {
            orderEmitter.send(message);
            System.out.println("Kafka message sent: " + message);
        } catch (Exception e) {
            System.err.println("Kafka'ya mesaj gönderilemedi: " + e.getMessage());
            e.printStackTrace();
        }
        // -----------------------------

        return order;
    }

    public List<Order> getMyOrders(Long userId) {
        return Order.list("buyerId", userId);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order.deleteById(orderId);
    }
}