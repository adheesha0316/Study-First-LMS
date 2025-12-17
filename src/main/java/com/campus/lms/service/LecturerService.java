package com.campus.lms.service;

import com.campus.lms.dto.*;

import java.util.List;

public interface LecturerService {
    // Profile
    LecturerDto createLecturer(LecturerDto dto);
    LecturerDto getLecturerById(Integer lecturerId);
    LecturerDto updateLecturer(Integer lecturerId, LecturerDto dto);
    void deleteLecturer(Integer lecturerId);
    List<LecturerDto> getAllLecturers();

    // Online lecture
    OnlineLectureViewDto createOnlineLecture(Integer lecturerId, OnlineLectureCreateDto dto);

    // Assignment management
    String uploadAssignmentQuestion(Integer lecturerId, AssignmentUploadDto dto);
    List<AssignmentSubmissionDto> getSubmissions(Integer assignmentId);
    String gradeAssignment(Integer submissionId, Double marks);
}
