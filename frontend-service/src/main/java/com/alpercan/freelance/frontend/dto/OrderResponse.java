package com.alpercan.freelance.frontend.dto;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long buyerId,
        Long gigId,
        OrderStatus status,
        LocalDateTime createdAt
) {}