package com.alpercan.freelance.gig.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gigs")
public class Gig extends PanacheEntity {

    public Long sellerId; // İlanı açan kullanıcının ID'si (User Service'ten gelecek)

    public String title;
    public String description;
    public BigDecimal price;
    public String category; // Örn: "Web Development", "Logo Design"
    public String pictureUrl; // İlanın kapak fotoğrafı

    public LocalDateTime createdAt;

    // Otomatik tarih atama (Constructor veya PrePersist ile de yapılabilir)
    public Gig() {
        this.createdAt = LocalDateTime.now();
    }
}