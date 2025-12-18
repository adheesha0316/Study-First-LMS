package com.campus.lms.repo;

import com.campus.lms.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentSubmissionRepo extends JpaRepository<AssignmentSubmission, Integer> {
    List<AssignmentSubmission> findByAssignment_AssignmentId(Integer assignmentId);
    List<AssignmentSubmission> findByStudent_StudentId(Integer studentId);
}
