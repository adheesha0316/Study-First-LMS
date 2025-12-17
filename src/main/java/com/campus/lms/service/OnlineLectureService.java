package com.campus.lms.service;

import com.campus.lms.dto.OnlineLectureCreateDto;
import com.campus.lms.dto.OnlineLectureViewDto;

import java.util.List;

public interface OnlineLectureService {
    String createOnlineLecture(Integer lecturerId, OnlineLectureCreateDto dto);

    List<OnlineLectureViewDto> getMyLectures(Integer lecturerId);

    String cancelLecture(Integer lecturerId, Integer lectureId);
}
