
package com.example.buildtrack.user.dto;

import com.example.buildtrack.user.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {

    private Role role;
}
