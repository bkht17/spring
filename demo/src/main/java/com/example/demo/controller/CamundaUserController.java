package com.example.demo.controller;

import com.example.demo.dto.CamundaUserDto;
import com.example.demo.service.CamundaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camunda/users")
public class CamundaUserController {

    @Autowired
    private CamundaUserService camundaUserService;

    @GetMapping("/{id}")
    public ResponseEntity<CamundaUserDto> get(@PathVariable("id") String id) {
        CamundaUserDto user = camundaUserService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<CamundaUserDto>> getAll() {
        return ResponseEntity.ok(camundaUserService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CamundaUserDto>> search(@RequestParam String q) {
        return ResponseEntity.ok(camundaUserService.searchUsers(q));
    }

    @PostMapping("")
    public ResponseEntity<CamundaUserDto> create(@RequestBody CamundaUserDto user) {
        try {
            CamundaUserDto createdUser = camundaUserService.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamundaUserDto> update(@PathVariable("id") String id, @RequestBody CamundaUserDto user) {
        try {
            CamundaUserDto updatedUser = camundaUserService.update(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        try {
            camundaUserService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}