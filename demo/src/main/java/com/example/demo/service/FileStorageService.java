package com.example.demo.service;

import com.example.demo.dto.FileDto;
import com.example.demo.entity.UploadedFileEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileStorageService {
    UploadedFileEntity uploadFile(MultipartFile file) throws IOException;
    ResponseEntity<byte[]> downloadFile(Long id);
    UploadedFileEntity getFileById(Long id);
    void deleteFile(Long id);
    List<FileDto> getAllFiles();
}
