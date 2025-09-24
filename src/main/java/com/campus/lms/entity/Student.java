package com.campus.lms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    private String name;

    private int age;

    @Column(nullable = false, unique = true)
    private String nationalId;

    private String phone;

    private String address;

    private String profileImage; // file path (uploads/students/...)

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    // 🔗 Relationship with Course (Many students -> One course)
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    // Audit Fields
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
