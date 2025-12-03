package com.alpercan.freelance.frontend.dto;

public record SendMessageRequest(
        Long receiverId,
        String content
) {}