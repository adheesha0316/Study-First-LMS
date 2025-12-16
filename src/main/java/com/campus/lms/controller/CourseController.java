package com.campus.lms.controller;

import com.campus.lms.dto.CourseDto;
import com.campus.lms.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin
public class CourseController {
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ---------------- Student ----------------
    @GetMapping("/active")
    public ResponseEntity<List<CourseDto>> getActiveCourses() {
        return ResponseEntity.ok(courseService.getActiveCourses());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    // ---------------- Admin ----------------
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.createCourse(dto));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.updateCourse(courseId, dto));
    }

    @PatchMapping("/{courseId}/status/{status}")
    public ResponseEntity<String> changeStatus(
            @PathVariable Integer courseId,
            @PathVariable String status) {
        return ResponseEntity.ok(courseService.changeCourseStatus(courseId, status));
    }
}
