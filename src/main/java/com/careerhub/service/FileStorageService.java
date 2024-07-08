package com.careerhub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    @Value("${upload.path}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String firstName, String lastName) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = firstName + lastName + "_cv_" + System.currentTimeMillis() + ".pdf";
        Path targetLocation = uploadPath.resolve(fileName);

        // Copy file to the target location
        Files.copy(file.getInputStream(), targetLocation);

        // Return the relative file path
        return uploadDir + "/" + fileName;
    }
}
