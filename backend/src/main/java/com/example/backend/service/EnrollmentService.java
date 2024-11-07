package com.example.backend.service;

import com.example.backend.dto.CourseDTO;
import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.EnrollmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseRepository courseRepository;

    public Map<CourseDTO, Long> getWeeklyEnrollmentReport(LocalDate startDate, LocalDate endDate) {
        // Lấy tất cả các khóa học
        List<Course> allCourses = courseRepository.findAll();

        // Lấy danh sách đăng ký trong khoảng thời gian từ startDate đến endDate
        List<Enrollment> enrollments = enrollmentRepository.findByRegistrationDateBetweenAndActive(startDate, endDate, true);

        // Tạo Map để lưu trữ số lượng đăng ký cho mỗi khóa học
        Map<Course, Long> enrollmentCountMap = enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getCourse, Collectors.counting()));

        // Đảm bảo tất cả các khóa học đều có mặt trong Map, kể cả những khóa học không có đăng ký
        Map<CourseDTO, Long> reportData = new HashMap<>();
        for (Course course : allCourses) {
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
            reportData.put(courseDTO, enrollmentCountMap.getOrDefault(course, 0L));
        }

        return reportData;
    }
}

