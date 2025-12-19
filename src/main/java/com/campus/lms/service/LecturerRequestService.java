package com.campus.lms.service;

import com.campus.lms.dto.LecturerRequestDto;
import com.campus.lms.entity.LecturerRequest;


import java.util.List;

public interface LecturerRequestService {
    // ================= USER → request lecturer role =================
    LecturerRequestDto createLecturerRequest(LecturerRequestDto requestDto, String userEmail);

    // ================= ADMIN → view all requests =================
    List<LecturerRequestDto> getAllRequests();

    // ================= ADMIN → approve lecturer =================
    void approveLecturerRequest(Integer requestId, String adminEmail);

    // ================= ADMIN → reject lecturer =================
    void rejectLecturerRequest(Integer requestId, String adminEmail);
}
