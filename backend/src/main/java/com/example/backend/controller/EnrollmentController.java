package com.example.backend.controller;

import com.example.backend.dto.CourseDTO;
import com.example.backend.service.EnrollmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/my-courses")
    public List<CourseDTO> getRegisteredCourses(@RequestParam String username) {
        return enrollmentService.getRegisteredCourses(username);
    }

    @DeleteMapping("/{courseId}/cancel")
    public ResponseEntity<String> cancelRegistration(@PathVariable Integer courseId, @RequestParam String username) {
        enrollmentService.cancelRegistration(courseId, username);
        return ResponseEntity.ok("Course registration canceled successfully");
    }
}