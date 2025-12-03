package com.alpercan.freelance.frontend.dto;

public record ProfileRequest(
        String bio,
        String pictureUrl,
        String website,
        String location
) {}