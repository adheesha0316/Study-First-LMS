package com.campus.lms.repo;

import com.campus.lms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {

    // find by course name
    Optional<Course> findByName(String name);

    // check duplicate course name
    boolean existsByName(String name);

    // check duplicate course code
    boolean existsByCode(String code);
}
