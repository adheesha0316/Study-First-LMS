package com.campus.lms.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AssignmentUploadDto {
    private Integer courseId;
    private MultipartFile file; // Assignment file to upload
    private String title;
    private String description;
}
