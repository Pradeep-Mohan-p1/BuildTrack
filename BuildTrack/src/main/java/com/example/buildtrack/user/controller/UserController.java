
package com.example.buildtrack.user.controller;

import com.example.buildtrack.user.dto.RegisterRequest;
import com.example.buildtrack.user.dto.RoleUpdateRequest;
import com.example.buildtrack.user.dto.UserResponse;
import com.example.buildtrack.user.entity.Role;
import com.example.buildtrack.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Public registration: new users receive EMPLOYEE role
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(request));
    }

    // Admin creates a user with a selected role
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody RegisterRequest request,
            @RequestParam Role role) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUserWithRole(request, role));
    }

    // Admin gets all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Admin gets user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Admin changes user role
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest request) {

        return ResponseEntity.ok(
                userService.updateUserRole(id, request.getRole()));
    }

    // Admin deactivates user
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    // Admin activates user
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.activateUser(id));
    }

}

