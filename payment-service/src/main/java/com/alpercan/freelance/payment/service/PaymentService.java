package com.alpercan.freelance.payment.service;

import com.alpercan.freelance.payment.dto.CreateOrderRequest;
import com.alpercan.freelance.payment.model.Order;
import com.alpercan.freelance.payment.model.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PaymentService {

    @Transactional
    public Order createOrder(Long buyerId, CreateOrderRequest request) {
        Order order = new Order();
        order.buyerId = buyerId;
        order.gigId = request.gigId();
        // Normalde burada Gig Service'e gidip fiyatı sormamız lazım ama MVP'de atlıyoruz.

        order.persist();
        return order;
    }

    @Transactional
    public Order completePayment(Long orderId) {
        Order order = Order.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }

        // MOCK ÖDEME: Sanki Stripe'a gittik, para çekildi ve onay geldi gibi davranıyoruz.
        order.status = OrderStatus.COMPLETED;

        return order; // Değişiklik otomatik kaydedilir (Transactional sayesinde)
    }

    public List<Order> getMyOrders(Long userId) {
        return Order.list("buyerId", userId);
    }
}