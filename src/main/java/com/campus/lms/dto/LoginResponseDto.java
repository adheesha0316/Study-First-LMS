package com.campus.lms.dto;

import com.campus.lms.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String email;
    private String token;
    private Role role;
    private String userName;
    private String status;
}
