package com.example.demo.dto.mapping;

import com.example.demo.entity.UploadedFileEntity;
import com.example.demo.dto.FileDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapping {
    FileDto toFileDto(UploadedFileEntity file);
    List<FileDto> toFileDtos(List<UploadedFileEntity> files);
}