package com.campus.lms.repo;

import com.campus.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> getUsersByEmail(String emil);
}
