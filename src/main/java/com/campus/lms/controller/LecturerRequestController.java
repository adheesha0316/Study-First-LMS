package com.campus.lms.controller;

import com.campus.lms.dto.LecturerRequestDto;
import com.campus.lms.entity.LecturerRequest;
import com.campus.lms.service.LecturerRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lecturer-requests")
@CrossOrigin
public class LecturerRequestController {
    private final LecturerRequestService lecturerRequestService;
    private final ModelMapper modelMapper;

    @Autowired
    public LecturerRequestController(LecturerRequestService lecturerRequestService, ModelMapper modelMapper) {
        this.lecturerRequestService = lecturerRequestService;
        this.modelMapper = modelMapper;
    }

    // ================= USER: submit lecturer request =================
    @PostMapping("/submit")
    public ResponseEntity<String> createRequest(
            @RequestBody LecturerRequestDto dto,
            Principal principal
    ) {
        lecturerRequestService.createLecturerRequest(dto, principal.getName());
        return ResponseEntity.ok("Lecturer request submitted");
    }

    // ================= USER: cancel own request =================
    @DeleteMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelRequest(
            @PathVariable Integer requestId,
            @RequestParam String userEmail) {

        lecturerRequestService.deleteRequest(requestId, userEmail);
        return ResponseEntity.ok("Lecturer request cancelled successfully");
    }

    // ================= USER: get single lecturer request by id =================
    @GetMapping("/{requestId}")
    public ResponseEntity<LecturerRequestDto> getRequestById(
            @PathVariable Integer requestId) {

        LecturerRequest request = lecturerRequestService.getRequestById(requestId);
        LecturerRequestDto dto = modelMapper.map(request, LecturerRequestDto.class);

        return ResponseEntity.ok(dto);
    }


    // ================= ADMIN: view all requests =================
    @GetMapping("/getAll")
    public ResponseEntity<List<LecturerRequestDto>> getAllRequests() {
        List<LecturerRequest> requests = lecturerRequestService.getAllRequests();
        List<LecturerRequestDto> dtos = requests.stream()
                .map(req -> modelMapper.map(req, LecturerRequestDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ================= ADMIN: approve request =================
    @PostMapping("/approve/{requestId}")
    public ResponseEntity<String> approveRequest(
            @PathVariable Integer requestId,
            @RequestParam String adminEmail) {

        lecturerRequestService.approveLecturerRequest(requestId, adminEmail);
        return ResponseEntity.ok("Lecturer request approved successfully");
    }

    // ================= ADMIN: reject request =================
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectRequest(
            @PathVariable Integer requestId,
            @RequestParam String adminEmail) {

        lecturerRequestService.rejectLecturerRequest(requestId, adminEmail);
        return ResponseEntity.ok("Lecturer request rejected successfully");
    }
}
