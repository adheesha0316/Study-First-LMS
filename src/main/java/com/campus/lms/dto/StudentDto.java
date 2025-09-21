package com.campus.lms.dto;

import org.springframework.web.multipart.MultipartFile;

public class StudentDto {
    private String email;   // link User email
    private String name;
    private int age;
    private String nationalId;
    private String phone;
    private String course;
    private String address;
    private MultipartFile profileImage;
}
