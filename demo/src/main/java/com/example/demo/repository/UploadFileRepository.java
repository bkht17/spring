package com.example.demo.repository;

import com.example.demo.entity.UploadedFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadedFileEntity, Long> {
}
