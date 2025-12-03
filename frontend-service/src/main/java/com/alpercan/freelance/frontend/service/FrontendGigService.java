package com.alpercan.freelance.frontend.service;

import com.alpercan.freelance.frontend.client.GigServiceClient;
import com.alpercan.freelance.frontend.dto.GigRequest;
import com.alpercan.freelance.frontend.dto.GigResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class FrontendGigService {

    @Inject
    @RestClient
    GigServiceClient gigServiceClient;

    @Inject
    FileStorageService fileStorageService;

    // --- 1. GIG OLUŞTURMA ---
    public void createGig(String token, String title, String description, BigDecimal price, String category, FileUpload pictureFile) {
        String pictureUrl = fileStorageService.saveFile(pictureFile);

        GigRequest request = new GigRequest(title, description, price, category, pictureUrl);

        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        gigServiceClient.createGig(token, request);
    }

    // --- 2. TÜM GIGLER (Parametresiz - Null gönderir) ---
    public List<GigResponse> getAllGigs() {
        return gigServiceClient.getAllGigs(null, null);
    }

    // --- 3. TÜM GIGLER (Filtreli) ---
    public List<GigResponse> getAllGigs(String category, String sort) {
        return gigServiceClient.getAllGigs(category, sort);
    }

    // --- 4. BENİM GIGLERİM ---
    public List<GigResponse> getMyGigs(String token) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        return gigServiceClient.getMyGigs(token);
    }

    // --- 5. GIG VAR MI KONTROLÜ ---
    public boolean hasGigs(String token) {
        try {
            List<GigResponse> myGigs = getMyGigs(token);
            return !myGigs.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // --- 6. ÖNE ÇIKANLAR ---
    public List<GigResponse> getFeaturedGigs() {
        try {
            List<GigResponse> allGigs = gigServiceClient.getAllGigs(null, null);
            if (allGigs == null) return Collections.emptyList();
            return allGigs.stream().limit(6).toList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    public void deleteGig(String token, Long id) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        gigServiceClient.deleteGig(token, id);
    }
}