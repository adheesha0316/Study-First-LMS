package com.campus.lms.service.Impl;

import com.campus.lms.dto.LecturerRequestDto;
import com.campus.lms.entity.Lecturer;
import com.campus.lms.entity.LecturerRequest;
import com.campus.lms.entity.User;
import com.campus.lms.enums.LecturerRequestStatus;
import com.campus.lms.enums.Role;
import com.campus.lms.repo.LecturerRepo;
import com.campus.lms.repo.LecturerRequestRepo;
import com.campus.lms.repo.UserRepo;
import com.campus.lms.service.LecturerRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LecturerRequestServiceImpl implements LecturerRequestService {
    private final LecturerRequestRepo lecturerRequestRepo;
    private final LecturerRepo lecturerRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public LecturerRequestServiceImpl(LecturerRequestRepo lecturerRequestRepo, LecturerRepo lecturerRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.lecturerRequestRepo = lecturerRequestRepo;
        this.lecturerRepo = lecturerRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }


    // ================= USER → request lecturer role =================

    @Override
    public LecturerRequestDto createLecturerRequest(LecturerRequestDto requestDto, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (lecturerRequestRepo.existsByUser_UserId(user.getUserId())) {
            throw new RuntimeException("Lecturer request already submitted");
        }

        LecturerRequest request = modelMapper.map(requestDto, LecturerRequest.class);
        request.setUser(user);
        request.setStatus(LecturerRequestStatus.PENDING);

        LecturerRequest saved = lecturerRequestRepo.save(request);
        return modelMapper.map(saved, LecturerRequestDto.class);
    }

    // ================= ADMIN → view all requests =================

    @Override
    public List<LecturerRequestDto> getAllRequests() {
        return lecturerRequestRepo.findAll()
                .stream()
                .map(req -> modelMapper.map(req, LecturerRequestDto.class))
                .collect(Collectors.toList());
    }

    // ================= ADMIN → approve lecturer =================

    @Override
    public void approveLecturerRequest(Integer requestId, String adminEmail) {
        LecturerRequest request = lecturerRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User user = request.getUser();

        // Update user role
        user.setRole(Role.LECTURER);
        userRepo.save(user);

        // Create lecturer
        Lecturer lecturer = Lecturer.builder()
                .user(user)
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .highestQualification(request.getHighestQualification())
                .specialization(request.getSpecialization())
                .yearsOfExperience(request.getYearsOfExperience())
                .designation(request.getDesignation())
                .department(request.getDepartment())
                .bio(request.getBio())
                .achievements(request.getAchievements())
                .createdBy(adminEmail)
                .active(true)
                .build();

        lecturerRepo.save(lecturer);

        // Remove request
        lecturerRequestRepo.delete(request);
    }

    // ================= ADMIN → reject lecturer =================

    @Override
    public void rejectLecturerRequest(Integer requestId, String adminEmail) {
        LecturerRequest request = lecturerRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));


        request.setStatus(LecturerRequestStatus.REJECTED);
        request.setReviewedBy(adminEmail);

        lecturerRequestRepo.save(request);
    }
}
