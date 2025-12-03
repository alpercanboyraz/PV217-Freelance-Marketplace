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


    private static final String UPLOAD_DIR = "uploads/";

    public String saveFile(FileUpload fileUpload) {
        if (fileUpload == null || fileUpload.fileName() == null || fileUpload.fileName().isEmpty()) {
            return null; // Resim yoksa null dön (HTML'de varsayılanı gösteririz)
        }

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = fileUpload.fileName();

            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path targetPath = Paths.get(UPLOAD_DIR + uniqueFilename);


            Files.move(fileUpload.uploadedFile(), targetPath, StandardCopyOption.REPLACE_EXISTING);


            return "/uploads/" + uniqueFilename;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }
}