package com.example.demo.dto.mapping.mappingimpl;

import com.example.demo.dto.FileDto;
import com.example.demo.dto.mapping.FileMapping;
import com.example.demo.entity.UploadedFileEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileMappingImpl implements FileMapping {
    @Override
    public FileDto toFileDto(UploadedFileEntity file) {
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setFilename(file.getFilename());
        fileDto.setContentType(file.getContentType());

        return fileDto;
    }

    @Override
    public List<FileDto> toFileDtos(List<UploadedFileEntity> files) {
        return files.stream().map(this::toFileDto).collect(Collectors.toList());
    }
}
