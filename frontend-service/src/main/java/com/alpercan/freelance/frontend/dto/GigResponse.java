package com.alpercan.freelance.frontend.dto;

import java.math.BigDecimal;

public record GigResponse(
        Long id,
        Long sellerId,
        String title,
        String description,
        BigDecimal price,
        String category,
        String pictureUrl
) {}