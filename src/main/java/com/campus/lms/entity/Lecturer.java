package com.campus.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lecturers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lecturerId;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String address;

    // Store only file path (ex: uploads/lecturers/profile123.png)
    private String profileImage;

    // Academic details
    @Column(nullable = false)
    private String highestQualification; // e.g., PhD, MSc, BSc

    private String specialization; // e.g., AI, Data Science, Networking

    private int yearsOfExperience;

    private String designation; // e.g., Senior Lecturer, Professor, Assistant Lecturer

    private String department; // e.g., Computer Science, Engineering

    private String bio; // Short description about lecturer

    private String achievements; // Notable publications, awards, etc.

    private boolean active = true; // Whether lecturer is currently active

    // 🔗 One-to-One with User (authentication details like email & password stored in User)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonIgnore
    private User user;

    // 🔗 Many-to-Many with Course
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lecturer_courses",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    // Audit fields
    private String createdBy;
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Auto timestamps
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
