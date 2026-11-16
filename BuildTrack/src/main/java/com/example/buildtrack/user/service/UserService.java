
package com.example.buildtrack.user.service;

import com.example.buildtrack.user.dto.RegisterRequest;
import com.example.buildtrack.user.dto.UserResponse;
import com.example.buildtrack.user.entity.Role;
import com.example.buildtrack.user.entity.User;
import com.example.buildtrack.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Convert Entity to Response DTO
    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // Register a new employee-level user
    public UserResponse registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.EMPLOYEE)
                .enabled(true)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    // Create users with a specified role
    // This method is called only by an authorized Admin endpoint
    public UserResponse createUserWithRole(
            RegisterRequest request, Role role) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(true)
                .build();

        return mapToResponse(userRepository.save(user));
    }

    // Get all users
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get user by ID
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = findUser(id);

        return mapToResponse(user);
    }

    // Update user role
    public UserResponse updateUserRole(Long id, Role role) {

        User user = findUser(id);

        user.setRole(role);

        return mapToResponse(userRepository.save(user));
    }

    // Deactivate user
    public UserResponse deactivateUser(Long id) {

        User user = findUser(id);

        user.setEnabled(false);

        return mapToResponse(userRepository.save(user));
    }

    // Activate user
    public UserResponse activateUser(Long id) {

        User user = findUser(id);

        user.setEnabled(true);

        return mapToResponse(userRepository.save(user));
    }

    private User findUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with ID: " + id));
    }
}
