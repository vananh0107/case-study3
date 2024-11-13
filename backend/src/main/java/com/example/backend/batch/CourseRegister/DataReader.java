package com.example.backend.batch.CourseRegister;

import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;

import java.time.LocalDate;
import java.util.List;

public class DataReader {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Course> allCourses;
    private List<Enrollment> enrollments;

    public DataReader(LocalDate startDate, LocalDate endDate, List<Course> allCourses, List<Enrollment> enrollments) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.allCourses = allCourses;
        this.enrollments = enrollments;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<Course> getAllCourses() { return allCourses; }
    public List<Enrollment> getEnrollments() { return enrollments; }
}