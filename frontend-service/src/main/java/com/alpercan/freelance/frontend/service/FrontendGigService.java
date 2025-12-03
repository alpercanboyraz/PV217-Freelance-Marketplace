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

    public void createGig(String token, String title, String description, BigDecimal price, String category, FileUpload pictureFile) {
        String pictureUrl = fileStorageService.saveFile(pictureFile);

        GigRequest request = new GigRequest(title, description, price, category, pictureUrl);

        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        gigServiceClient.createGig(token, request);
    }

    public List<GigResponse> getAllGigs() {
        return gigServiceClient.getAllGigs();
    }

    // --- İŞTE EKSİK OLAN METOT BUYDU ---
    public List<GigResponse> getMyGigs(String token) {
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        return gigServiceClient.getMyGigs(token);
    }

    public boolean hasGigs(String token) {
        try {
            // Yukarıdaki metodu kullanarak kontrol ediyoruz
            List<GigResponse> myGigs = getMyGigs(token);
            return !myGigs.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public List<GigResponse> getFeaturedGigs() {
        try {
            List<GigResponse> allGigs = gigServiceClient.getAllGigs();
            if (allGigs == null) return Collections.emptyList();
            return allGigs.stream().limit(6).toList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    public void deleteGig(String token, Long id) {
        if (!token.startsWith("Bearer ")) token = "Bearer " + token;
        gigServiceClient.deleteGig(token, id);
    }
}