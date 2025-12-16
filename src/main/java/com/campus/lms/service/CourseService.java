package com.campus.lms.service;

import com.campus.lms.dto.CourseDto;

import java.util.List;

public interface CourseService {
    // Admin / Lecturer
    CourseDto createCourse(CourseDto dto);
    CourseDto updateCourse(Integer courseId, CourseDto dto);
    String changeCourseStatus(Integer courseId, String status);

    // Student
    List<CourseDto> getActiveCourses();
    CourseDto getCourseById(Integer courseId);
}
