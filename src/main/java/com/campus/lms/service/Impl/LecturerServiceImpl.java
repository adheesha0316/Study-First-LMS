package com.campus.lms.service.Impl;

import com.campus.lms.dto.*;
import com.campus.lms.entity.*;
import com.campus.lms.enums.LectureStatus;
import com.campus.lms.repo.*;
import com.campus.lms.service.LecturerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LecturerServiceImpl implements LecturerService {
    private final LecturerRepo lecturerRepo;
    private final OnlineLectureRepo onlineLectureRepo;
    private final CourseRepo courseRepo;
    private final AssignmentRepo assignmentRepo;
    private final AssignmentSubmissionRepo submissionRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public LecturerServiceImpl(LecturerRepo lecturerRepo, OnlineLectureRepo onlineLectureRepo, CourseRepo courseRepo, AssignmentRepo assignmentRepo, AssignmentSubmissionRepo submissionRepo, ModelMapper modelMapper) {
        this.lecturerRepo = lecturerRepo;
        this.onlineLectureRepo = onlineLectureRepo;
        this.courseRepo = courseRepo;
        this.assignmentRepo = assignmentRepo;
        this.submissionRepo = submissionRepo;
        this.modelMapper = modelMapper;
    }

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.upload.lecturer-profile:lectuerProfileImg}")
    private String lecturerProfileFolder;


    private static final String DEFAULT_IMAGE =
            "uploads/lectuerProfileImg/default.png";

    // ------------------- Lecturer Profile -------------------
    @Override
    public LecturerDto createLecturer(LecturerDto dto) {
        Lecturer lecturer = modelMapper.map(dto, Lecturer.class);
        Lecturer saved = lecturerRepo.save(lecturer);
        return modelMapper.map(saved, LecturerDto.class);
    }

    @Override
    public LecturerDto getLecturerById(Integer lecturerId) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        LecturerDto dto = modelMapper.map(lecturer, LecturerDto.class);
        Set<OnlineLectureViewDto> lectures = onlineLectureRepo.findByLecturer_LecturerId(lecturerId)
                .stream()
                .map(l -> modelMapper.map(l, OnlineLectureViewDto.class))
                .collect(Collectors.toSet());
        dto.setLectures(lectures);
        return dto;
    }

    @Override
    public LecturerDto updateLecturer(Integer lecturerId, LecturerDto dto) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Update fields
        lecturer.setName(dto.getName());
        lecturer.setPhone(dto.getPhone());
        lecturer.setAddress(dto.getAddress());
        lecturer.setProfileImage(dto.getProfileImage());
        lecturer.setHighestQualification(dto.getHighestQualification());
        lecturer.setSpecialization(dto.getSpecialization());
        lecturer.setYearsOfExperience(dto.getYearsOfExperience());
        lecturer.setDesignation(dto.getDesignation());
        lecturer.setDepartment(dto.getDepartment());
        lecturer.setBio(dto.getBio());
        lecturer.setAchievements(dto.getAchievements());
        lecturer.setActive(dto.isActive());

        Lecturer updated = lecturerRepo.save(lecturer);
        return modelMapper.map(updated, LecturerDto.class);
    }

    @Override
    public void uploadOrUpdateProfileImage(Integer lecturerId, MultipartFile image) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Check if file is empty
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }

        // Validate content type (jpg/png)
        String contentType = image.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new RuntimeException("Only JPG and PNG images are allowed");
        }

        // Validate max file size (2 MB)
        long maxFileSize = 2 * 1024 * 1024; // 2 MB
        if (image.getSize() > maxFileSize) {
            throw new RuntimeException("File size exceeds 2 MB limit");
        }

        try {
            // Directory path
            Path directoryPath = Paths.get(uploadDir, lecturerProfileFolder);
            Files.createDirectories(directoryPath);

            // File name
            String fileName = "lecturer_" + lecturerId + "_" +
                    System.currentTimeMillis() + "_" +
                    image.getOriginalFilename();

            Path newFilePath = directoryPath.resolve(fileName);

            // Delete old image (if not default)
            if (lecturer.getProfileImage() != null &&
                    !lecturer.getProfileImage().equals(DEFAULT_IMAGE)) {

                Path oldImagePath = Paths.get(uploadDir)
                        .resolve(lecturer.getProfileImage().replace("uploads/", ""));
                Files.deleteIfExists(oldImagePath);
            }

            // Save new image
            Files.copy(image.getInputStream(), newFilePath,
                    StandardCopyOption.REPLACE_EXISTING);

            // Save relative path in DB
            lecturer.setProfileImage("uploads/" + lecturerProfileFolder + "/" + fileName);
            lecturerRepo.save(lecturer);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload lecturer profile image", e);
        }
    }

    @Override
    public void uploadOrUpdateProfileImageSecure(Integer lecturerId, MultipartFile image, String loggedInEmail) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        // Security check
        if (!lecturer.getUser().getEmail().equals(loggedInEmail)) {
            throw new RuntimeException("You are not allowed to update this profile");
        }

        // Reuse existing logic
        uploadOrUpdateProfileImage(lecturerId, image);
    }


    @Override
    public void deleteLecturer(Integer lecturerId) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));
        lecturerRepo.delete(lecturer);
    }

    @Override
    public List<LecturerDto> getAllLecturers() {
        return lecturerRepo.findAll()
                .stream()
                .map(l -> modelMapper.map(l, LecturerDto.class))
                .collect(Collectors.toList());
    }

    // ------------------- Online Lecture -------------------
    @Override
    public OnlineLectureViewDto createOnlineLecture(Integer lecturerId, OnlineLectureCreateDto dto) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        OnlineLecture lecture = modelMapper.map(dto, OnlineLecture.class);
        lecture.setLecturer(lecturer);
        lecture.setCourse(course);
        lecture.setStatus(LectureStatus.UPCOMING);

        OnlineLecture saved = onlineLectureRepo.save(lecture);
        return modelMapper.map(saved, OnlineLectureViewDto.class);
    }

    // ------------------- Assignment Management -------------------
    @Override
    public String uploadAssignmentQuestion(Integer lecturerId, AssignmentUploadDto dto) {
        Lecturer lecturer = lecturerRepo.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // File path (file save logic should be in util/service)
        String filePath = "uploads/assignments/questions/"
                + System.currentTimeMillis()
                + "_" + dto.getFile().getOriginalFilename();

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .filePath(filePath)
                .lecturer(lecturer)
                .course(course)
                .build();

        assignmentRepo.save(assignment);

        return "Assignment uploaded successfully";
    }

    @Override
    public List<AssignmentSubmissionDto> getSubmissions(Integer assignmentId) {
        return submissionRepo.findByAssignment_AssignmentId(assignmentId)
                .stream()
                .map(sub -> AssignmentSubmissionDto.builder()
                        .assignmentId(sub.getAssignment().getAssignmentId())
                        .studentId(sub.getStudent().getStudentId())
                        .studentName(sub.getStudent().getName())
                        .filePath(sub.getFilePath())
                        .marks(sub.getMarks())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String gradeAssignment(Integer submissionId, Double marks) {
        AssignmentSubmission submission = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setMarks(marks);
        submissionRepo.save(submission);

        return "Marks updated successfully";
    }
}
