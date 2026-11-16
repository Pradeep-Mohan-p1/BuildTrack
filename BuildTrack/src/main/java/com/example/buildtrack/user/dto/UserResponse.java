
package com.example.buildtrack.user.dto;

import com.example.buildtrack.user.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private Boolean enabled;

    private LocalDateTime createdAt;
}

