package com.campus.lms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnlineLectureCreateDto {
    private String title;
    private String meetingLink;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer courseId;
}
