package com.alpercan.freelance.chat.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message extends PanacheEntity {

    public Long senderId;   // Gönderen (Token'dan gelecek)
    public Long receiverId; // Alan
    public String content;  // Mesaj içeriği
    public LocalDateTime sentAt;

    public Message() {
        this.sentAt = LocalDateTime.now();
    }
}