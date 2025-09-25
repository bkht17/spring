package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_file_id")
    private UploadedFileEntity uploadedFile;

}
