package com.alpercan.freelance.user.service;

import com.alpercan.freelance.user.dto.LoginRequest;     // EKLENDİ
import com.alpercan.freelance.user.dto.RegisterRequest;
import com.alpercan.freelance.user.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;                       // EKLENDİ
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Arrays;                                  // EKLENDİ
import java.util.HashSet;

@ApplicationScoped // Bu sınıfın Quarkus tarafından yönetilen bir servis olduğunu belirtir
public class UserService {

    @Transactional
    public User register(RegisterRequest request) {
        // Email kontrolü (Aynı email ile kayıt olunmasın)
        if (User.find("email", request.email()).firstResult() != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.username = request.username();
        // ŞİFREYİ HASH'LEME İŞLEMİ BURADA!
        user.password = BcryptUtil.bcryptHash(request.password());
        user.email = request.email();
        user.fullName = request.fullName();
        user.role = request.role();

        user.persist();
        return user;
    }

    public String login(LoginRequest request) {
        // 1. Kullanıcıyı bul
        User user = User.find("email", request.email()).firstResult();

        // 2. Kullanıcı yoksa veya şifre yanlışsa hata fırlat
        if (user == null || !BcryptUtil.matches(request.password(), user.password)) {
            throw new SecurityException("Invalid email or password");
        }

        // 3. Her şey doğruysa Token (Kimlik Kartı) üret
        // Bu token, kullanıcının kim olduğunu (email) ve yetkisini (role) taşır.
        return Jwt.issuer("https://freelance-market.com")
                .upn(user.email) // User Principal Name (Kullanıcı kimliği)
                .groups(new HashSet<>(Arrays.asList(user.role))) // Rolü (FREELANCER vs)
                .expiresIn(3600) // 1 saat geçerli olsun
                .sign();
    }
}