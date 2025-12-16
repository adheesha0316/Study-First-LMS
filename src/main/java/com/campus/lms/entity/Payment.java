package com.campus.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Double amount;

    private String paymentSlip; // file path

    private LocalDateTime paymentDate;

    private String status; // e.g., "PENDING", "PAID", "FAILED"

    @PrePersist
    public void onPayment() {
        this.paymentDate = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }
}
