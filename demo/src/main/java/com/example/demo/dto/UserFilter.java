package com.example.demo.dto;

public record UserFilter(
        String firstname,
        String lastname,
        Integer age,
        Integer page,
        Integer size
) {
    public UserFilter {
        page = page != null ? page : 0;
        size = size != null ? size : 10;
    }
}