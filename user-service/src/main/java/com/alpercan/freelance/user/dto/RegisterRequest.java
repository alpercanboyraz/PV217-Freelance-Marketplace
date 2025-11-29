package com.alpercan.freelance.user.dto;

// Java Record: Veri taşıyan, değişmez (immutable) ve kısa sınıflar için idealdir.
// Getter/Setter yazmana gerek kalmaz.
public record RegisterRequest(
    String username,
    String password,
    String email,
    String fullName,
    String role
) {}