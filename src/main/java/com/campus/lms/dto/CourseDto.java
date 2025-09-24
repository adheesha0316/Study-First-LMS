package com.campus.lms.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Integer courseId;
    private String courseName;
    private String description;
    private int durationMonths;
    private double fee;
}
