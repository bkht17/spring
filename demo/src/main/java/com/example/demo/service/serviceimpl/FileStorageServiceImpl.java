package com.example.demo.service.serviceimpl;

import com.example.demo.entity.UploadedFileEntity;
import com.example.demo.repository.UploadFileRepository;
import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Override
    public UploadedFileEntity uploadFile(MultipartFile file) throws IOException {
        UploadedFileEntity uploadedFile = new UploadedFileEntity();
        uploadedFile.setFilename(file.getOriginalFilename());
        uploadedFile.setContentType(file.getContentType());
        uploadedFile.setData(file.getBytes());
        return uploadFileRepository.save(uploadedFile);
    }

    @Override
    public UploadedFileEntity getFileById(Long id) {
        return uploadFileRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteFile(Long id) {
        uploadFileRepository.deleteById(id);
    }

    @Override
    public UploadedFileEntity updateFile(Long id, MultipartFile file) throws IOException {
        UploadedFileEntity existingFile = getFileById(id);

        existingFile.setFilename(file.getOriginalFilename());
        existingFile.setContentType(file.getContentType());
        existingFile.setData(file.getBytes());
        existingFile.setUploadedAt(new java.util.Date());

        return uploadFileRepository.save(existingFile);
    }
}
