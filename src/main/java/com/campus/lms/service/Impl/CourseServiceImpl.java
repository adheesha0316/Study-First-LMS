package com.campus.lms.service.Impl;

import com.campus.lms.dto.CourseDto;
import com.campus.lms.entity.Course;
import com.campus.lms.enums.CourseStatus;
import com.campus.lms.repo.CourseRepo;
import com.campus.lms.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public CourseServiceImpl(CourseRepo courseRepo, ModelMapper modelMapper) {
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
    }

    // ---------------- Admin ----------------
    @Override
    public CourseDto createCourse(CourseDto dto) {
        Course course = modelMapper.map(dto, Course.class);
        course.setStatus(CourseStatus.ACTIVE);
        return modelMapper.map(courseRepo.save(course), CourseDto.class);
    }

    @Override
    public CourseDto updateCourse(Integer courseId, CourseDto dto) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setDurationMonths(dto.getDurationMonths());
        course.setFee(dto.getFee());

        return modelMapper.map(courseRepo.save(course), CourseDto.class);
    }

    @Override
    public String changeCourseStatus(Integer courseId, String status) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setStatus(CourseStatus.valueOf(status.toUpperCase()));
        courseRepo.save(course);

        return "Course status updated to " + status;
    }


    // ---------------- Student ----------------
    @Override
    public List<CourseDto> getActiveCourses() {
        return courseRepo.findByStatus(CourseStatus.ACTIVE)
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new RuntimeException("Course is not active");
        }

        return modelMapper.map(course, CourseDto.class);
    }
}
