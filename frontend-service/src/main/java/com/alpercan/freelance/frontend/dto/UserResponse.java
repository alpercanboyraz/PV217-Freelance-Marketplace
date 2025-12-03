package com.alpercan.freelance.frontend.dto;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String bio,
        String pictureUrl
) {}