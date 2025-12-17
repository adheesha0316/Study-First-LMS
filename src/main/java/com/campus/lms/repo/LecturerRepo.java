package com.campus.lms.repo;

import com.campus.lms.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerRepo extends JpaRepository<Lecturer, Integer> {
    // Find by name
    Optional<Lecturer> findByName(String name);

    // Check if lecturer exists by name
    boolean existsByName(String name);

    // Check if lecturer exists by user id
    boolean existsByUser_UserId(Integer userId);

    // Find by user id (for profile or authentication purposes)
    Optional<Lecturer> findByUser_UserId(Integer userId);
}
