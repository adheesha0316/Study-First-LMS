package com.campus.lms.service;

import com.campus.lms.dto.OnlineLectureCreateDto;
import com.campus.lms.dto.OnlineLectureViewDto;

import java.util.List;

public interface OnlineLectureService {
    // Create an online lecture and return its details
    OnlineLectureViewDto createOnlineLecture(Integer lecturerId, OnlineLectureCreateDto dto);

    // Get all lectures created by a lecturer
    List<OnlineLectureViewDto> getMyLectures(Integer lecturerId);

    // Cancel a lecture (return success message)
    String cancelLecture(Integer lecturerId, Integer lectureId);
}
