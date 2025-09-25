package com.example.demo.repository;

import com.example.demo.dto.UserFilter;
import com.example.demo.entity.UserEntity;

import java.util.List;

public interface FilterUserRepository {
    List<UserEntity> findAllByFilter(UserFilter filter);

}
