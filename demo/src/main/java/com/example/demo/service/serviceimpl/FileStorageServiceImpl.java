package com.example.demo.service.serviceimpl;

import com.example.demo.dto.FileDto;
import com.example.demo.dto.mapping.FileMapping;
import com.example.demo.entity.UploadedFileEntity;
import com.example.demo.repository.UploadFileRepository;
import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    UploadFileRepository uploadFileRepository;

    @Autowired
    FileMapping fileMapping;

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
    public List<FileDto> getAllFiles() {
        List<UploadedFileEntity> files = uploadFileRepository.findAll();
        return fileMapping.toFileDtos(files);
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long id) {
        UploadedFileEntity file = getFileById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getData());
    }
}
