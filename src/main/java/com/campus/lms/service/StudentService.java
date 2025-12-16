package com.campus.lms.service;

import com.campus.lms.dto.*;

import java.util.List;

public interface StudentService {
    // -------------------- Student Profile --------------------
    StudentRegisterDto registerStudent(StudentRegisterDto dto);

    StudentRegisterDto getProfile(Integer studentId);

    StudentRegisterDto updateProfile(Integer studentId, StudentUpdateDto dto);

    // -------------------- Profile Image --------------------
    String uploadProfileImage(Integer studentId, StudentProfileImageDto dto);

    // -------------------- Course Enrollment --------------------
    String enrollCourse(Integer studentId, EnrollmentDto dto);

    String unenrollCourse(Integer studentId, Integer courseId);

    // -------------------- Payment --------------------
    String uploadPaymentSlip(Integer studentId, PaymentUploadDto dto);

    // -------------------- Courses & Materials --------------------
    List<CourseDto> getAllCourses();

    List<String> getCourseMaterials(Integer courseId);

    List<String> getCourseRecordings(Integer courseId);

    List<String> getAssignments(Integer courseId);

    List<String> getQuizzes(Integer courseId);

    List<String> getExams(Integer courseId);
}
