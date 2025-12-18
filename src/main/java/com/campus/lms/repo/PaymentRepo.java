package com.campus.lms.repo;

import com.campus.lms.entity.Payment;
import com.campus.lms.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    // Admin – view pending / approved / rejected payments
    List<Payment> findByStatus(PaymentStatus status);

    // Optional: payments by student
    List<Payment> findByStudent_StudentId(Integer studentId);

    // Optional: payments by course
    List<Payment> findByCourse_CourseId(Integer courseId);
}
