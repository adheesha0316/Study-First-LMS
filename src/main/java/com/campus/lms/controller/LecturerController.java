package com.campus.lms.controller;

import com.campus.lms.dto.*;
import com.campus.lms.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerController {

    private final LecturerService lecturerService;

    @Autowired
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    // ------------------- Lecturer Profile -------------------
    @PostMapping("/register")
    public ResponseEntity<LecturerDto> createLecturer(@RequestBody LecturerDto dto) {
        return ResponseEntity.ok(lecturerService.createLecturer(dto));
    }

    @GetMapping("/{lecturerId}/profile")
    public ResponseEntity<LecturerDto> getLecturerById(@PathVariable Integer lecturerId) {
        return ResponseEntity.ok(lecturerService.getLecturerById(lecturerId));
    }

    @PutMapping("/update/{lecturerId}")
    public ResponseEntity<LecturerDto> updateLecturer(
            @PathVariable Integer lecturerId,
            @RequestBody LecturerDto dto) {
        return ResponseEntity.ok(lecturerService.updateLecturer(lecturerId, dto));
    }

    @DeleteMapping("/delete/{lecturerId}")
    public ResponseEntity<String> deleteLecturer(@PathVariable Integer lecturerId) {
        lecturerService.deleteLecturer(lecturerId);
        return ResponseEntity.ok("Lecturer deleted successfully");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LecturerDto>> getAllLecturers() {
        return ResponseEntity.ok(lecturerService.getAllLecturers());
    }

    // ------------------- Online Lecture -------------------

    @PostMapping("/{lecturerId}/lectures")
    public ResponseEntity<OnlineLectureViewDto> createOnlineLecture(
            @PathVariable Integer lecturerId,
            @RequestBody OnlineLectureCreateDto dto) {
        return ResponseEntity.ok(lecturerService.createOnlineLecture(lecturerId, dto));
    }

    // ------------------- Assignment Management -------------------

    @PostMapping("/{lecturerId}/assignments")
    public ResponseEntity<String> uploadAssignment(
            @PathVariable Integer lecturerId,
            @ModelAttribute AssignmentUploadDto dto) {
        return ResponseEntity.ok(lecturerService.uploadAssignmentQuestion(lecturerId, dto));
    }

    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<List<AssignmentSubmissionDto>> getSubmissions(
            @PathVariable Integer assignmentId) {
        return ResponseEntity.ok(lecturerService.getSubmissions(assignmentId));
    }

    @PutMapping("/assignments/submissions/{submissionId}/grade")
    public ResponseEntity<String> gradeAssignment(
            @PathVariable Integer submissionId,
            @RequestParam Double marks) {
        return ResponseEntity.ok(lecturerService.gradeAssignment(submissionId, marks));
    }
}
