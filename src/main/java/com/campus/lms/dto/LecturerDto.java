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
public class LecturerDto {
    private Integer lecturerId;
    private String name;
    private String phone;
    private String address;
    private String profileImage;
    private String highestQualification;
    private String specialization;
    private int yearsOfExperience;
    private String designation;
    private String department;
    private String bio;
    private String achievements;
    private boolean active;

    // Optional: include lectures created by the lecturer
    private Set<OnlineLectureViewDto> lectures;
}
