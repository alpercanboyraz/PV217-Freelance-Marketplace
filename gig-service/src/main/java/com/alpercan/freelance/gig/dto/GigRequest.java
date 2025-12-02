package com.alpercan.freelance.gig.dto;

import java.math.BigDecimal;

public record GigRequest(
        String title,
        String description,
        BigDecimal price,
        String category,
        String pictureUrl
) {}