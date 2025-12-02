package com.alpercan.freelance.user.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    public String username;
    public String password;
    public String email;
    public String fullName;
    public String role;
    public String bio;          // Örn: "5 yıllık Java geliştiricisiyim..."
    public String pictureUrl;   // Örn: "https://my-bucket/alper.jpg"
    public String website;      // Örn: "https://alpercan.com"
    public String location;     // Örn: "Istanbul, Turkey"
}