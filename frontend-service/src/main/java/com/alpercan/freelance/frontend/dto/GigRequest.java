package com.alpercan.freelance.frontend.dto;

import java.math.BigDecimal;

public record GigRequest(
        String title,
        String description,
        BigDecimal price,
        String category,
        String pictureUrl
) {}