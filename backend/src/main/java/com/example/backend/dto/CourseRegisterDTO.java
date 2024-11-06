package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class CourseRegisterDTO {
    private Integer id;
    private String name;
    private String description;
    private int maxStudents;
    private LocalDate startDate;
    private Long currentStudentCount;
}
