package com.campus.lms.service;

import com.campus.lms.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {
    String enrollStudent(Integer studentId, EnrollmentDto dto);

    String cancelEnrollment(Integer studentId, Integer courseId);

    List<String> getStudentEnrollments(Integer studentId);

    List<String> getCourseEnrollments(Integer courseId);
}
