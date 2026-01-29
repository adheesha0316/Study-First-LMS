package com.campus.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUpdateDto {
    private String name;
    private int age;
    private String phone;
    private String address;
}
