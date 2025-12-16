package com.campus.lms.repo;

import com.campus.lms.entity.Enrollment;
import com.campus.lms.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepo extends JpaRepository<Enrollment, Integer> {
    Optional<Enrollment> findByStudent_StudentIdAndCourse_CourseId(
            Integer studentId, Integer courseId
    );

    List<Enrollment> findByStudent_StudentId(Integer studentId);

    List<Enrollment> findByCourse_CourseId(Integer courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);
}
