package com.campus.lms.service.Impl;

import com.campus.lms.dto.LoginRequestDto;
import com.campus.lms.dto.LoginResponseDto;
import com.campus.lms.dto.UserDto;
import com.campus.lms.dto.UserDtoReturn;
import com.campus.lms.entity.User;
import com.campus.lms.repo.UserRepo;
import com.campus.lms.service.UserService;
import com.campus.lms.utill.JWTTokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenGenerator jwtTokenGenerator;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, JWTTokenGenerator jwtTokenGenerator) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    // ---------------- REGISTER USER ---------------- //
    @Override
    public UserDtoReturn registerUser(UserDto userDto) {
        // Check if email already exists
        Optional<User> existingUser = userRepo.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            return new UserDtoReturn(userDto.getEmail(), "User already exists");
        }

        // Map UserDto → User
        User user = modelMapper.map(userDto, User.class);

        // Encrypt password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Save user
        User savedUser = userRepo.save(user);

        return new UserDtoReturn(savedUser.getEmail(), "Registered Successfully");
    }

    // ---------------- LOGIN ---------------- //
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<User> userOptional = userRepo.findByEmail(loginRequestDto.getEmail());

        if (userOptional.isEmpty()) {
            return new LoginResponseDto(null, null, null, null, "Invalid credentials");
        }

        User user = userOptional.get();

        // Check password
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return new LoginResponseDto(null, null, null, null, "Invalid credentials");
        }

        // Generate JWT token
        String token = jwtTokenGenerator.generateToken(user);

        // Map response
        return LoginResponseDto.builder()
                .email(user.getEmail())
                .userName(user.getUsername())
                .role(user.getRole())
                .token(token)
                .status("Login successful")
                .build();
    }

    // ---------------- FIND USER BY EMAIL ---------------- //
    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
