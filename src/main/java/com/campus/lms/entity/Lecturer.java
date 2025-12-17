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

    @Builder.Default
    private String profileImage = "uploads/lecturers/default.png";

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

    // 🔗 One-to-One with User (authentication info like email & password stored in User)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonIgnore
    private User user;

    // 🔗 Many-to-Many with Courses
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "lecturer_courses",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnore
    @Builder.Default
    private Set<Course> courses = new HashSet<>();

    // 🔗 One-to-Many: Online lectures created by this lecturer
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<OnlineLecture> onlineLectures = new HashSet<>();

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

    // Optional: helper methods
    public void addCourse(Course course) {
        courses.add(course);
        course.getLecturers().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getLecturers().remove(this);
    }

    public void addOnlineLecture(OnlineLecture lecture) {
        onlineLectures.add(lecture);
        lecture.setLecturer(this);
    }

    public void removeOnlineLecture(OnlineLecture lecture) {
        onlineLectures.remove(lecture);
        lecture.setLecturer(null);
    }
}
