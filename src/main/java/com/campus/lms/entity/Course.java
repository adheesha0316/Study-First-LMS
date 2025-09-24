package com.campus.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false, unique = true)
    private String courseName;

    @Column(nullable = false)
    private String description;

    private int durationMonths; // course duration in months
    private double fee;

    // 🔗 One course can have many students
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    // Audit fields
    private String createdBy;
    private String updatedBy;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
