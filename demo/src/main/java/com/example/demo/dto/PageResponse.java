package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;

    public PageResponse(List<T> content, int currentPage, int totalPages, long totalElements, int pageSize) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
    }
}