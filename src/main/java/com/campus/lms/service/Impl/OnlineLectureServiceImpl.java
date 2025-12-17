package com.campus.lms.service.Impl;

import com.campus.lms.dto.OnlineLectureCreateDto;
import com.campus.lms.dto.OnlineLectureViewDto;
import com.campus.lms.entity.Course;
import com.campus.lms.entity.Lecturer;
import com.campus.lms.entity.OnlineLecture;
import com.campus.lms.enums.LectureStatus;
import com.campus.lms.repo.CourseRepo;
import com.campus.lms.repo.LecturerRepo;
import com.campus.lms.repo.OnlineLectureRepo;
import com.campus.lms.service.OnlineLectureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public OnlineLectureViewDto createOnlineLecture(Integer lecturerId, OnlineLectureCreateDto dto) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));
        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        OnlineLecture lecture = modelMapper.map(dto, OnlineLecture.class);
        lecture.setLecturer(lecturer);
        lecture.setCourse(course);

        OnlineLecture saved = onlineLectureRepo.save(lecture); // correct variable name
        return modelMapper.map(saved, OnlineLectureViewDto.class);
    }

    @Override
    public List<OnlineLectureViewDto> getMyLectures(Integer lecturerId) {
        List<OnlineLecture> lectures = onlineLectureRepo.findByLecturer_LecturerId(lecturerId);
        return lectures.stream()
                .map(lecture -> modelMapper.map(lecture, OnlineLectureViewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public String cancelLecture(Integer lecturerId, Integer lectureId) {
        OnlineLecture lecture = onlineLectureRepo.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        if (!lecture.getLecturer().getLecturerId().equals(lecturerId)) {
            throw new RuntimeException("You are not authorized to cancel this lecture");
        }

        lecture.setStatus(LectureStatus.CANCELLED);
        onlineLectureRepo.save(lecture);

        return "Lecture cancelled successfully!";
    }
}
