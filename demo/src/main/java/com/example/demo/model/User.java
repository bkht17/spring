package com.example.demo.model;


public record User (
    Long id,
    String firstname,
    String lastname,
    String email,
    String password,
    int age
){}
