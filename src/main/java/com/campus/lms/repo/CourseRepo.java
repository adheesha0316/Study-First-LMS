package com.campus.lms.repo;

import com.campus.lms.entity.Course;
import com.campus.lms.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {

    // find by course name
    Optional<Course> findByCourseName(String courseName);

    // check duplicate course name
    boolean existsByCourseName(String courseName);

    // get courses by status (ACTIVE / INACTIVE / ARCHIVED)
    List<Course> findByStatus(CourseStatus status);
}
