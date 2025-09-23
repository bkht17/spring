package com.example.demo.service;

import com.example.demo.entity.UploadedFileEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    UploadedFileEntity uploadFile(MultipartFile file) throws IOException;

    ResponseEntity<byte[]> downloadFile(Long id);

    UploadedFileEntity getFileById(Long id);
    void deleteFile(Long id);
}
