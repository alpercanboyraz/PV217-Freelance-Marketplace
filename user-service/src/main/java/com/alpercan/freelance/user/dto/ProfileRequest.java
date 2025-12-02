package com.alpercan.freelance.user.dto;

public record ProfileRequest(
        String bio,
        String pictureUrl,
        String website,
        String location
) {}