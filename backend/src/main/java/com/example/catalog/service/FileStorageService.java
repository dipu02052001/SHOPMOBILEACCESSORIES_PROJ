package com.example.catalog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

  private final Path uploadDir;

  public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) throws IOException {
    this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    Files.createDirectories(this.uploadDir);
  }

  public String store(MultipartFile file) {
    if (file == null || file.isEmpty()) throw new IllegalArgumentException("Empty file");
    String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
    String ext = "";
    int i = original.lastIndexOf('.');
    if (i >= 0) ext = original.substring(i);
    String filename = UUID.randomUUID().toString().replace("-", "") + ext.toLowerCase();
    Path target = uploadDir.resolve(filename);
    try {
      Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
      return filename;
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file", e);
    }
  }

  public Path resolve(String filename){
    return uploadDir.resolve(filename).normalize();
  }
}
