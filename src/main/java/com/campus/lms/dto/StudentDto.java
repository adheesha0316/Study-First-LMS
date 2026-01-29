package com.campus.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private Integer studentId;
    private String email;
    private String name;
    private int age;
    private String nationalId;
    private String phone;
    private String address;
    private String profileImage;
    private String paymentSlip;
    private Set<CourseDto> courses; // optional, if front-end wants course list
}
