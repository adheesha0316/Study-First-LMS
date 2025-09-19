package com.campus.lms.service;

import com.campus.lms.dto.LoginRequestDto;
import com.campus.lms.dto.LoginResponseDto;
import com.campus.lms.dto.UserDto;
import com.campus.lms.dto.UserDtoReturn;
import com.campus.lms.entity.User;

public interface UserService {
    /**
     * Register a new user (Student, Lecturer, or Admin)
     */
    UserDtoReturn registerUser(UserDto userDto);

    /**
     * Login a user and generate JWT token
     */
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    /**
     * Find user by email
     */
    User findByEmail(String email);
}
