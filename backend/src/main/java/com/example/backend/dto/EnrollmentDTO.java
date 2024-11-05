package com.example.backend.dto;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Integer id;

    private Integer studentId;

    private Integer courseId;

    private boolean active;

}
