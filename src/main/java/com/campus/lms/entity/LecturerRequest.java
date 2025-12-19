package com.campus.lms.entity;

import com.campus.lms.enums.LecturerRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecturer_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    // 🔗 User
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Lecturer info
    private String name;
    private String phone;
    private String address;
    private String highestQualification;
    private String specialization;
    private int yearsOfExperience;
    private String designation;
    private String department;
    private String bio;
    private String achievements;

    // Request status
    @Enumerated(EnumType.STRING)
    private LecturerRequestStatus status;

    // Optional: which admin reviewed
    private String reviewedBy;

    private String createdBy;
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() { this.createdAt = LocalDateTime.now(); }

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}
