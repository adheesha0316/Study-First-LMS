package com.campus.lms.controller;

import com.campus.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.campus.lms.dto.EnrollmentDto;


import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@CrossOrigin
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    //  Enroll student to course
    @PostMapping("/student/{studentId}")
    public ResponseEntity<String> enroll(
            @PathVariable Integer studentId,
            @RequestBody EnrollmentDto dto
    ) {
        return ResponseEntity.ok(
                enrollmentService.enrollStudent(studentId, dto)
        );
    }

    // Cancel enrollment
    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<String> cancel(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId
    ) {
        return ResponseEntity.ok(
                enrollmentService.cancelEnrollment(studentId, courseId)
        );
    }

    // Student's enrolled courses
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<String>> studentEnrollments(
            @PathVariable Integer studentId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getStudentEnrollments(studentId)
        );
    }

    // Course enrolled students
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<String>> courseEnrollments(
            @PathVariable Integer courseId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getCourseEnrollments(courseId)
        );
    }
}
