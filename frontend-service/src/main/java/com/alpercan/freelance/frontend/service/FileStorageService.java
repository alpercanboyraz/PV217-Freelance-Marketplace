package com.alpercan.freelance.frontend.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@ApplicationScoped
public class FileStorageService {

    // Quarkus'un statik dosyaları sunduğu özel klasör yolu (Dev modda burası çalışır)
    // Not: Canlı ortamda bu yolu değiştirmek gerekebilir.
    private static final String UPLOAD_DIR = "src/main/resources/META-INF/resources/uploads/";

    public String saveFile(FileUpload fileUpload) {
        if (fileUpload == null || fileUpload.fileName() == null) {
            // Eğer dosya seçilmediyse varsayılan bir resim dön
            return "https://via.placeholder.com/400x300?text=No+Image";
        }

        try {
            // 1. Yükleme klasörünün var olduğundan emin ol
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 2. Benzersiz bir dosya adı oluştur (Çakışmayı önlemek için UUID kullanıyoruz)
            // Örnek: "myimage.jpg" -> "550e8400-e29b-41d4-a716-446655440000_myimage.jpg"
            String originalFilename = fileUpload.fileName();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path targetPath = Paths.get(UPLOAD_DIR + uniqueFilename);

            // 3. Dosyayı geçici yerden kalıcı yerine taşı
            Files.move(fileUpload.uploadedFile(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 4. Dosyanın erişilebilir URL'ini döndür
            // Quarkus, META-INF/resources altındaki dosyaları direkt root'tan sunar.
            return "/uploads/" + uniqueFilename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }
}