package com.campus.lms.service;

import com.campus.lms.dto.LecturerRequestDto;
import com.campus.lms.entity.LecturerRequest;


import java.util.List;

public interface LecturerRequestService {
    // ================= USER → request lecturer role =================
    void createLecturerRequest(LecturerRequestDto dto, String userEmail);

    // ================= USER → cancel request =================
    void deleteRequest(Integer requestId, String userEmail);

    // Optional: get a single request by id
    LecturerRequest getRequestById(Integer requestId);

    // ================= ADMIN → view all requests =================
    List<LecturerRequest> getAllRequests();

    // ================= ADMIN → approve lecturer =================
    void approveLecturerRequest(Integer requestId, String adminEmail);

    // ================= ADMIN → reject lecturer =================
    void rejectLecturerRequest(Integer requestId, String adminEmail);
}
