package com.campus.lms.entity;

import com.campus.lms.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // ✅ Store study materials, recordings, assignments, etc. (file paths / links)
    @ElementCollection
    private List<String> materials = new ArrayList<>();

    @ElementCollection
    private List<String> recordings = new ArrayList<>();

    @ElementCollection
    private List<String> assignments = new ArrayList<>();

    @ElementCollection
    private List<String> quizzes = new ArrayList<>();

    @ElementCollection
    private List<String> exams = new ArrayList<>();

    // 🔗 One course can have many students
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Enrollment> enrollments = new HashSet<>();

    // 🔗 One course can have many lecturers (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "course_lecturers",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id")
    )
    @JsonIgnore
    private Set<Lecturer> lecturers = new HashSet<>();

    // Add status field
    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.ACTIVE;

    // Audit fields
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
