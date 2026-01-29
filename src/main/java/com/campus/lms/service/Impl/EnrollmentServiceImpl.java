package com.campus.lms.service.Impl;

import com.campus.lms.dto.EnrollmentDto;
import com.campus.lms.entity.Course;
import com.campus.lms.entity.Enrollment;
import com.campus.lms.entity.Student;
import com.campus.lms.enums.CourseStatus;
import com.campus.lms.enums.EnrollmentStatus;
import com.campus.lms.repo.CourseRepo;
import com.campus.lms.repo.EnrollmentRepo;
import com.campus.lms.repo.StudentRepo;
import com.campus.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    @Override
    public String enrollStudent(Integer studentId, EnrollmentDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new RuntimeException("Course is not active");
        }

        enrollmentRepo.findByStudent_StudentIdAndCourse_CourseId(
                studentId, dto.getCourseId()
        ).ifPresent(e -> {
            throw new RuntimeException("Student already enrolled");
        });

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .build();

        enrollmentRepo.save(enrollment);

        return "Student enrolled successfully";
    }

    @Override
    public String cancelEnrollment(Integer studentId, Integer courseId) {
        Enrollment enrollment = enrollmentRepo
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepo.save(enrollment);

        return "Enrollment cancelled successfully";
    }

    @Override
    public List<String> getStudentEnrollments(Integer studentId) {
        return enrollmentRepo.findByStudent_StudentId(studentId)
                .stream()
                .map(e -> e.getCourse().getCourseName())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCourseEnrollments(Integer courseId) {
        return enrollmentRepo.findByCourse_CourseId(courseId)
                .stream()
                .map(e -> e.getStudent().getName())
                .collect(Collectors.toList());
    }
}
