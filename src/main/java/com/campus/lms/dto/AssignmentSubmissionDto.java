package com.campus.lms.dto;

import lombok.Data;

@Data
public class AssignmentSubmissionDto {
    private Integer assignmentId;
    private Integer studentId;
    private String studentName;
    private String filePath;
    private Double marks; // Lecturer can assign marks
}
