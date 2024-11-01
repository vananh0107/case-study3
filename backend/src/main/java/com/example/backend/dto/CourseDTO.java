package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseDTO {

    private Integer id;

    private String name;

    private String description;

    private int maxStudents;

    private LocalDate startDate;
}