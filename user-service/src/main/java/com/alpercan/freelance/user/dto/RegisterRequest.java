package com.alpercan.freelance.user.dto;

public record RegisterRequest(
    String username,
    String password,
    String email,
    String fullName,
    String role
) {}