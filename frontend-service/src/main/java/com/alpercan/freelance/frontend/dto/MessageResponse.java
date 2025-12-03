package com.alpercan.freelance.frontend.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long senderId,
        Long receiverId,
        String content,
        LocalDateTime sentAt
) {}