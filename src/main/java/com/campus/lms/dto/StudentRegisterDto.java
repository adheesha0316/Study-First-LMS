package com.campus.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRegisterDto {
    private Integer studentId;
    private String name;
    private int age;
    private String nationalId;
    private String phone;
    private String address;
    private String profileImage;
    private Integer userId;
}
