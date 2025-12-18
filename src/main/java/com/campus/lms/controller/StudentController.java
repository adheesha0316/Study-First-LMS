package com.campus.lms.controller;


import com.campus.lms.dto.*;
import com.campus.lms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {

        this.studentService = studentService;
    }

    // -------------------- Registration --------------------
    @PostMapping("/register")
    public ResponseEntity<StudentRegisterDto> registerStudent(@RequestBody StudentRegisterDto dto) {
        StudentRegisterDto registered = studentService.registerStudent(dto);
        return ResponseEntity.ok(registered);
    }

    // -------------------- Profile --------------------
    @GetMapping("/{studentId}/profile")
    public ResponseEntity<StudentRegisterDto> getProfile(@PathVariable("studentId") Integer studentId) {
        StudentRegisterDto profile = studentService.getProfile(studentId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/update/{studentId}")
    public ResponseEntity<StudentRegisterDto> updateProfile(
            @PathVariable("studentId") Integer studentId,
            @RequestBody StudentUpdateDto dto) {
        StudentRegisterDto updated = studentService.updateProfile(studentId, dto);
        return ResponseEntity.ok(updated);
    }

    // -------------------- Profile Image Upload --------------------
    @PostMapping("/upload/profile-image/{studentId}")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable("studentId") Integer studentId,
            @ModelAttribute StudentProfileImageDto dto) {
        String message = studentService.uploadProfileImage(studentId, dto);
        return ResponseEntity.ok(message);
    }

    // -------------------- Course Enrollment --------------------
    @PostMapping("/{studentId}/enroll")
    public ResponseEntity<String> enrollCourse(
            @PathVariable("studentId") Integer studentId,
            @RequestBody EnrollmentDto dto) {
        String message = studentService.enrollCourse(studentId, dto);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{studentId}/unenroll/{courseId}")
    public ResponseEntity<String> unenrollCourse(
            @PathVariable("studentId") Integer studentId,
            @PathVariable("courseId") Integer courseId) {
        String message = studentService.unenrollCourse(studentId, courseId);
        return ResponseEntity.ok(message);
    }

    // -------------------- Payment Upload --------------------
    @PostMapping("/{studentId}/payment")
    public ResponseEntity<String> uploadPayment(
            @PathVariable("studentId") Integer studentId,
            @ModelAttribute PaymentUploadDto dto) {
        String message = studentService.uploadPaymentSlip(studentId, dto);
        return ResponseEntity.ok(message);
    }

    // -------------------- Courses & Materials --------------------
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = studentService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{courseId}/materials")
    public ResponseEntity<List<String>> getCourseMaterials(@PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getCourseMaterials(courseId));
    }

    @GetMapping("/courses/{courseId}/recordings")
    public ResponseEntity<List<String>> getCourseRecordings(@PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getCourseRecordings(courseId));
    }

    @GetMapping("/courses/{courseId}/assignments")
    public ResponseEntity<List<String>> getAssignments(@PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getAssignments(courseId));
    }

    @GetMapping("/courses/{courseId}/quizzes")
    public ResponseEntity<List<String>> getQuizzes(@PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getQuizzes(courseId));
    }

    @GetMapping("/courses/{courseId}/exams")
    public ResponseEntity<List<String>> getExams(@PathVariable Integer courseId) {
        return ResponseEntity.ok(studentService.getExams(courseId));
    }

    // -------------------- Download Assignment --------------------
    @GetMapping("/{studentId}/assignments/{assignmentId}/download")
    public ResponseEntity<Resource> downloadAssignment(
            @PathVariable Integer studentId,
            @PathVariable Integer assignmentId) {

        Resource resource = studentService.downloadAssignment(studentId, assignmentId);

        String filename = resource.getFilename();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    // -------------------- Upload Submission --------------------
    @PostMapping("/{studentId}/assignments/{assignmentId}/submit")
    public ResponseEntity<String> submitAssignment(
            @PathVariable Integer studentId,
            @PathVariable Integer assignmentId,
            @ModelAttribute AssignmentSubmissionUploadDto dto) {

        String message = studentService.uploadAssignmentSubmission(studentId, assignmentId, dto);
        return ResponseEntity.ok(message);
    }
}
