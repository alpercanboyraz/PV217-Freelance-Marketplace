package com.alpercan.freelance.payment.model;

public enum OrderStatus {
    PENDING,    // Ödeme bekliyor
    COMPLETED,  // Ödeme alındı, iş başladı
    CANCELLED   // İptal edildi
}