package com.example.backend.controller;

import com.example.backend.dto.CourseDTO;
import com.example.backend.dto.StudentDTO;
import com.example.backend.service.CourseService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/api/all/courses")
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/api/all/courses/{courseId}/register")
    public ResponseEntity<String> registerCourse(@PathVariable Integer courseId, Authentication authentication) {
        String username = authentication.getName();
        courseService.registerCourse(courseId, username);
        return ResponseEntity.ok("Course registration successful");
    }
    @PostMapping("/api/all/courses/{courseId}/unregister")
    public ResponseEntity<String> unregisterCourse(@PathVariable Integer courseId, Authentication authentication) {
        String username = authentication.getName();
        courseService.unregisterCourse(courseId, username);
        return ResponseEntity.ok("Course unregistration successful");
    }
    @GetMapping("/api/all/courses/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Integer courseId) {
        CourseDTO courseDTO = courseService.getCourseById(courseId);
        return ResponseEntity.ok(courseDTO);
    }
    @GetMapping("/api/teacher/courses/{courseId}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsByCourse(@PathVariable Integer courseId) {
        List<StudentDTO> students = courseService.getStudentsByCourseId(courseId);
        return ResponseEntity.ok(students);
    }
    @PostMapping("/api/teacher/courses/create")
    public ResponseEntity<CourseDTO> addCourse(@RequestBody @Valid CourseDTO courseDTO) {
        CourseDTO newCourse = courseService.addCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @PutMapping("/api/teacher/courses/edit/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody @Valid CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/api/teacher/courses/delete/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/api/user/courses/my")
    public ResponseEntity<List<CourseDTO>> getCoursesForUser(Authentication authentication) {
        String username = authentication.getName();

        List<CourseDTO> courseDTOs = courseService.getActiveCoursesForUser(username);

        return ResponseEntity.ok(courseDTOs);
    }

}