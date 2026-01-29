package com.campus.lms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentViewDto {
    private Integer paymentId;
    private Integer studentId;
    private Integer courseId;

    private Double amount;
    private String slipPath;

    private LocalDateTime paidAt;
}
