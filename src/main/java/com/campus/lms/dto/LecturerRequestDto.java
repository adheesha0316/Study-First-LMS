package com.campus.lms.dto;

import com.campus.lms.enums.LecturerRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerRequestDto {
    private Integer requestId;

    // Lecturer info
    private String name;
    private String phone;
    private String address;
    private String highestQualification;
    private String specialization;
    private int yearsOfExperience;
    private String designation;
    private String department;
    private String bio;
    private String achievements;

    // Status: PENDING, APPROVED, REJECTED
    private LecturerRequestStatus status;

    // Admin who reviewed
    private String reviewedBy;

    // Audit
    private String createdBy;
    private String updatedBy;
}
