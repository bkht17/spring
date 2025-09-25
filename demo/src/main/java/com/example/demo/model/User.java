package com.example.demo.model;


import com.example.demo.entity.UploadedFileEntity;

public record User (
        Long id,
        String firstname,
        String lastname,
        String email,
        String password,
        int age,
        UploadedFileEntity uploadedFile
){}
