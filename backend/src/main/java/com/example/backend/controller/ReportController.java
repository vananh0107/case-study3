package com.example.backend.controller;

import com.example.backend.dto.WeeklyEnrollmentDTO;
import com.example.backend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/api/teacher/courses/report")
    public ResponseEntity<List<WeeklyEnrollmentDTO>> getWeeklyEnrollmentStats() {
        List<WeeklyEnrollmentDTO> stats = enrollmentService.getWeeklyEnrollmentStatsUntilNow();
        return ResponseEntity.ok(stats);
    }
}
