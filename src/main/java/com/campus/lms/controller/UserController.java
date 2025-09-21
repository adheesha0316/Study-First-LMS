package com.campus.lms.controller;

import com.campus.lms.dto.LoginRequestDto;
import com.campus.lms.dto.LoginResponseDto;
import com.campus.lms.dto.UserDto;
import com.campus.lms.dto.UserDtoReturn;
import com.campus.lms.entity.User;
import com.campus.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ---------------- REGISTER USER ---------------- //
    @PostMapping("/register")
    public ResponseEntity<UserDtoReturn> registerUser(@RequestBody UserDto userDto) {
        UserDtoReturn response = userService.registerUser(userDto);
        return ResponseEntity.ok(response);
    }

    // ---------------- LOGIN USER ---------------- //
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = userService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    // ---------------- GET USER BY EMAIL ---------------- //
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
