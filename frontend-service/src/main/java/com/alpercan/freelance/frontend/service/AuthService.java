package com.alpercan.freelance.frontend.service; // Eğer service paketi açtıysan

import com.alpercan.freelance.frontend.client.UserServiceClient;
import com.alpercan.freelance.frontend.dto.LoginRequest;
import com.alpercan.freelance.frontend.dto.RegisterRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AuthService {

    @Inject
    @RestClient
    UserServiceClient userServiceClient;

    // Kayıt mantığı
    public void register(String username, String password, String email, String fullName, String role) {
        RegisterRequest request = new RegisterRequest(username, password, email, fullName, role);
        userServiceClient.register(request);
    }

    // Giriş mantığı (Sadece Token döner, HTTP/Cookie bilmez)
    public String login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return userServiceClient.login(request);
    }
}