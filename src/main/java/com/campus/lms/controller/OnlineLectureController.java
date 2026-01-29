package com.campus.lms.controller;

import com.campus.lms.dto.OnlineLectureCreateDto;
import com.campus.lms.dto.OnlineLectureViewDto;
import com.campus.lms.service.OnlineLectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lectures")
@CrossOrigin
public class OnlineLectureController {
    private final OnlineLectureService onlineLectureService;

    @Autowired
    public OnlineLectureController(OnlineLectureService onlineLectureService) {
        this.onlineLectureService = onlineLectureService;
    }

    /**
     * Create a new online lecture by lecturer
     * @param lecturerId - ID of the lecturer
     * @param dto - lecture details
     * @return created lecture
     */
    @PostMapping("/create/{lecturerId}")
    public ResponseEntity<OnlineLectureViewDto> createLecture(
            @PathVariable Integer lecturerId,
            @RequestBody OnlineLectureCreateDto dto
    ) {
        OnlineLectureViewDto createdLecture = onlineLectureService.createOnlineLecture(lecturerId, dto);
        return ResponseEntity.ok(createdLecture);
    }

    /**
     * Get all lectures of a specific lecturer
     * @param lecturerId - ID of the lecturer
     * @return list of lectures
     */
    @GetMapping("/my-lectures/{lecturerId}")
    public ResponseEntity<List<OnlineLectureViewDto>> getMyLectures(@PathVariable Integer lecturerId) {
        List<OnlineLectureViewDto> lectures = onlineLectureService.getMyLectures(lecturerId);
        return ResponseEntity.ok(lectures);
    }

    /**
     * Cancel a lecture
     * @param lecturerId - ID of the lecturer
     * @param lectureId - ID of the lecture
     * @return confirmation message
     */
    @PutMapping("/cancel/{lecturerId}/{lectureId}")
    public ResponseEntity<String> cancelLecture(
            @PathVariable Integer lecturerId,
            @PathVariable Integer lectureId
    ) {
        String message = onlineLectureService.cancelLecture(lecturerId, lectureId);
        return ResponseEntity.ok(message);
    }
}
