package com.campus.lms.repo;

import com.campus.lms.entity.LecturerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRequestRepo extends JpaRepository<LecturerRequest, Integer> {
    boolean existsByUser_UserId(Integer userId);

}
