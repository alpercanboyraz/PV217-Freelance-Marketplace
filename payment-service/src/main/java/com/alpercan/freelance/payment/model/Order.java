package com.alpercan.freelance.payment.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {

    public Long buyerId;  // Satın alan (Token'dan gelecek)
    public Long gigId;    // Satın alınan ilan
    // Gerçek senaryoda burada fiyat bilgisi de tutulur ama MVP için basitleştirdik.

    @Enumerated(EnumType.STRING)
    public OrderStatus status;

    public LocalDateTime createdAt;

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }
}