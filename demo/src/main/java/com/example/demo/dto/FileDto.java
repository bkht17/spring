package com.example.demo.dto;

import lombok.Data;
import java.util.Date;

@Data
public class FileDto {
    private Long id;
    private String filename;
    private String contentType;
    private Date uploadedAt;

}