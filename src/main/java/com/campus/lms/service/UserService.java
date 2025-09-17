package com.campus.lms.service;

import com.campus.lms.dto.LoginRequestDto;
import com.campus.lms.dto.LoginResponseDto;
import com.campus.lms.entity.User;

public interface UserService {
    User registerUser(LoginRequestDto LoginRequestDto);
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    User findByEmail(String email);
}
