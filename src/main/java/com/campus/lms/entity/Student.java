package com.campus.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    @Column(nullable = false)
    private String name;

    private int age;

    @Column(nullable = false, unique = true)
    private String nationalId;

    private String phone;

    private String address;

    @Builder.Default
    private String profileImage = "uploads/studentProfileImg/default.png";

    // 🔗 One-to-One with User
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    @JsonIgnore
    private User user;

    // 🔗 One-to-Many: Enrollments
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<Enrollment> enrollments = new HashSet<>();

    // 🔗 One-to-Many: Payments
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<Payment> payments = new HashSet<>();

    // Audit Fields
    private String createdBy;
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // JPA lifecycle callbacks for audit
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Optional: helper methods for enrollments
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setStudent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setStudent(null);
    }

    // Optional: helper methods for payments
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setStudent(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setStudent(null);
    }
}
