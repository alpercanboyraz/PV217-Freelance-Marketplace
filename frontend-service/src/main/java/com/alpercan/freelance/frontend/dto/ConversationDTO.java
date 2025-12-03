package com.alpercan.freelance.frontend.dto;

public record ConversationDTO(
        Long userId,
        String fullName,
        String pictureUrl
) {}