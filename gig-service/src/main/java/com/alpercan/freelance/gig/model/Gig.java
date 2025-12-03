package com.alpercan.freelance.gig.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gigs")
public class Gig extends PanacheEntity {

    public Long sellerId;

    public String title;
    public String description;
    public BigDecimal price;
    public String category;
    public String pictureUrl;

    public LocalDateTime createdAt;


    public Gig() {
        this.createdAt = LocalDateTime.now();
    }
}