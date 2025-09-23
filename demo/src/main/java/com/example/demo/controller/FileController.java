package com.example.demo.controller;

import com.example.demo.dto.FileDto;
import com.example.demo.entity.UploadedFileEntity;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        return ResponseEntity.ok(fileStorageService.getAllFiles());
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        UploadedFileEntity uploadedFile = fileStorageService.uploadFile(file);

        userService.saveFileData(file.getInputStream(), uploadedFile.getId());

        return ResponseEntity.ok("Excel File and Data Saved into Database");
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        return fileStorageService.downloadFile(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileStorageService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
