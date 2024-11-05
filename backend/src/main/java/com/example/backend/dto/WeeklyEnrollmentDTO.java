package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WeeklyEnrollmentDTO {
    private Integer courseId;
    private String courseName;
    private Long enrollmentCount;
    private LocalDate startDate;
    private LocalDate endDate;

    public WeeklyEnrollmentDTO(Integer courseId, String courseName, Long enrollmentCount, LocalDate startDate, LocalDate endDate) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.enrollmentCount = enrollmentCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
