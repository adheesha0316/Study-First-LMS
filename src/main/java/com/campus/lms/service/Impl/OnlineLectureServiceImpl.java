package com.campus.lms.service.Impl;

import com.campus.lms.dto.OnlineLectureCreateDto;
import com.campus.lms.dto.OnlineLectureViewDto;
import com.campus.lms.repo.CourseRepo;
import com.campus.lms.repo.LecturerRepo;
import com.campus.lms.repo.OnlineLectureRepo;
import com.campus.lms.service.OnlineLectureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineLectureServiceImpl implements OnlineLectureService {
    private final OnlineLectureRepo onlineLectureRepo;
    private final LecturerRepo lecturerRepo;
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public OnlineLectureServiceImpl(OnlineLectureRepo onlineLectureRepo, LecturerRepo lecturerRepo, CourseRepo courseRepo, ModelMapper modelMapper) {
        this.onlineLectureRepo = onlineLectureRepo;
        this.lecturerRepo = lecturerRepo;
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public String createOnlineLecture(Integer lecturerId, OnlineLectureCreateDto dto) {
        return "";
    }

    @Override
    public List<OnlineLectureViewDto> getMyLectures(Integer lecturerId) {
        return List.of();
    }

    @Override
    public String cancelLecture(Integer lecturerId, Integer lectureId) {
        return "";
    }
}
