package com.campus.lms.entity;

import jakarta.persistence.*;

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
    private String course;
    private String address;
    private String profileImage; // file path

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;
}
