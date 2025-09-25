package com.example.demo.controller;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.UserFilter;
import com.example.demo.model.User;
import com.example.demo.dto.UserDto;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAll(UserFilter filter) {
        return ResponseEntity.ok(userService.getAll(filter));
    }

    @GetMapping("/page")
    public ResponseEntity<PageResponse<UserDto>> getAllWithPagination(UserFilter filter) {
        return ResponseEntity.ok(userService.getAllWithPagination(filter));
    }

    @PostMapping("")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        try{
            userService.delete(id);
            return ResponseEntity.ok().build();
        }catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }
}
