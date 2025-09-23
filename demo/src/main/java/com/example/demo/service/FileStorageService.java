package com.example.demo.service;

import com.example.demo.entity.UploadedFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    UploadedFileEntity uploadFile(MultipartFile file) throws IOException;
    UploadedFileEntity getFileById(Long id);
    void deleteFile(Long id);
    UploadedFileEntity updateFile(Long id, MultipartFile file) throws IOException;
}
