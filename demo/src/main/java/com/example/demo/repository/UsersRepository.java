package com.example.demo.repository;

import com.example.demo.dto.UserFilter;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UserEntity, Long>, FilterUserRepository {

}
