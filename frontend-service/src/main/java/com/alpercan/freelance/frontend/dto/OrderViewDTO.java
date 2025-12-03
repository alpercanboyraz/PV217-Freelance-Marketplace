package com.alpercan.freelance.frontend.dto;

public record OrderViewDTO(
        OrderResponse order,
        GigResponse gig // İlanın detaylarını (Başlık, Fiyat) burada tutacağız
) {}