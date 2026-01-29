package com.campus.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private Integer courseId;
    private String courseName;
    private String description;
    private int durationMonths;
    private double fee;
}
