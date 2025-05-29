package com.library.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(root);
    }

    public String store(MultipartFile file, String filename) throws IOException {
        Path dest = root.resolve(filename);
        try (var in = file.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }
        return filename;
    }
}
