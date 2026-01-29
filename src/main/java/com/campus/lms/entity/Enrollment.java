package com.campus.lms.entity;

import com.campus.lms.enums.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "course_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enrollmentId;

    // 🔗 Student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;

    // 🔗 Course
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;

    @PrePersist
    public void onEnroll() {
        this.enrolledAt = LocalDateTime.now();
    }
}
