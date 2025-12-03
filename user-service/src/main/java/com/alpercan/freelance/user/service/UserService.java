package com.alpercan.freelance.user.service;

import com.alpercan.freelance.user.dto.LoginRequest;
import com.alpercan.freelance.user.dto.ProfileRequest;
import com.alpercan.freelance.user.dto.RegisterRequest;
import com.alpercan.freelance.user.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class UserService {

    @Transactional
    public User register(RegisterRequest request) {
        if (User.find("email", request.email()).firstResult() != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.username = request.username();
        user.password = BcryptUtil.bcryptHash(request.password());
        user.email = request.email();
        user.fullName = request.fullName();
        user.role = request.role();

        user.persist();
        return user;
    }

    public String login(LoginRequest request) {
        User user = User.find("email", request.email()).firstResult();

        if (user == null || !BcryptUtil.matches(request.password(), user.password)) {
            throw new SecurityException("Invalid email or password");
        }

        return Jwt.issuer("https://freelance-market.com")
                .upn(user.email)
                .groups(new HashSet<>(Arrays.asList(user.role)))
                .claim("userId", user.id)
                .expiresIn(3600)
                .sign();
    }

    public User getProfile(String email) {
        return User.find("email", email).firstResult();
    }

    @Transactional
    public User updateProfile(String email, ProfileRequest request) {
        User user = getProfile(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (request.bio() != null) user.bio = request.bio();
        if (request.pictureUrl() != null) user.pictureUrl = request.pictureUrl();
        if (request.website() != null) user.website = request.website();
        if (request.location() != null) user.location = request.location();

        return user;
    }
}