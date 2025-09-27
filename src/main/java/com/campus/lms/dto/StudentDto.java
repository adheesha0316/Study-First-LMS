package com.campus.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private String email;   // link User email
    private String name;
    private int age;
    private String nationalId;
    private String phone;
    private String address;
    private MultipartFile profileImage;
    private String paymentSlip;
    private Integer courseId;  // 🔗 FK reference
}
