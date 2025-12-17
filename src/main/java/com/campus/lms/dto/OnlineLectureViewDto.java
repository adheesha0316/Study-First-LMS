package com.campus.lms.dto;

import com.campus.lms.enums.LectureStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnlineLectureViewDto {
    private Integer lectureId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LectureStatus status;
}
