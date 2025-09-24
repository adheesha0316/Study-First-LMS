package com.campus.lms.repo;

import com.campus.lms.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo {
    // find by National ID
    Optional<Student> findByNationalId(String nationalId);

    // find by UserId (for profile related queries)
    Optional<Student> findByUser_UserId(Integer userId);

    // check duplicate phone
    boolean existsByPhone(String phone);

    // check duplicate email via User
    boolean existsByUser_Email(String email);
}
