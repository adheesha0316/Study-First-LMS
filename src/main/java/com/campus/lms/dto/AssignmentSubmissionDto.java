package com.campus.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmissionDto {
    private Integer assignmentId;
    private Integer studentId;
    private String studentName;
    private String filePath;
    private Double marks; // Lecturer can assign marks
}
