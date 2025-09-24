package com.campus.lms.service;

import com.campus.lms.dto.CourseDto;
import com.campus.lms.dto.StudentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    // Profile
    StudentDto getProfile(Integer studentId);
    StudentDto updateProfile(Integer studentId, StudentDto studentDto);

    // Courses
    List<CourseDto> getAllCourses();
    String enrollCourse(Integer studentId, Integer courseId);
    String unenrollCourse(Integer studentId, Integer courseId);

    // Materials & Recordings (view only)
    List<String> getCourseMaterials(Integer courseId);
    List<String> getCourseRecordings(Integer courseId);

    // Exams / Assignments / Quizzes (view only)
    List<String> getAssignments(Integer courseId);
    List<String> getQuizzes(Integer courseId);
    List<String> getExams(Integer courseId);

    // Payment
    String uploadPaymentSlip(Integer studentId, MultipartFile file);
}
