package com.alpercan.freelance.chat.dto;

public record SendMessageRequest(
        Long receiverId,
        String content
) {}