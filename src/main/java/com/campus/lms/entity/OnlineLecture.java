package com.campus.lms.entity;

import com.campus.lms.enums.LectureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "online_lectures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lectureId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String meetingLink; // Zoom / Google Meet

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private LectureStatus status;

    // 🔗 Lecturer who created the lecture
    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    // 🔗 Course
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = LectureStatus.UPCOMING;
    }
}
