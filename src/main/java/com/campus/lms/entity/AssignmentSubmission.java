package com.campus.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer submissionId;

    // Assignment
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    // Student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // REQUIRED
    @Column(nullable = false)
    private String filePath;   // submitted file path

    private Double marks;

    private LocalDateTime submittedAt;

    @PrePersist
    public void onSubmit() {
        this.submittedAt = LocalDateTime.now();
    }
}
