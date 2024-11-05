package com.example.backend.service;

import com.example.backend.dto.CourseDTO;
import com.example.backend.dto.WeeklyEnrollmentDTO;
import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.pojo.User;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.EnrollmentRepository;
import com.example.backend.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseRepository courseRepository;

    public List<CourseDTO> getRegisteredCourses(String username) {
        User student = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        return enrollments.stream()
                .map(enrollment -> modelMapper.map(enrollment.getCourse(), CourseDTO.class))
                .collect(Collectors.toList());
    }

    public void cancelRegistration(Integer courseId, String username) {
        User student = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentAndCourse(student, course);
        if (enrollmentOpt.isPresent() && enrollmentOpt.get().getCourse().getStartDate().isAfter(LocalDate.now())) {
            enrollmentRepository.delete(enrollmentOpt.get());
        } else {
            throw new RuntimeException("Cannot cancel registration");
        }

    }
    public List<WeeklyEnrollmentDTO> getWeeklyEnrollmentStatsUntilNow() {
        LocalDate earliestRegistrationDate = enrollmentRepository.findEarliestRegistrationDate();

        // Kiểm tra nếu không có người đăng ký
        if (earliestRegistrationDate == null) {
            throw new NoSuchElementException("No registrations found.");
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY).minusWeeks(1); // Đến tuần trước

        List<WeeklyEnrollmentDTO> stats = new ArrayList<>();

        // Tính toán các tuần từ ngày đầu tiên có người đăng ký
        LocalDate currentStartDate = earliestRegistrationDate;

        while (currentStartDate.isBefore(startOfWeek)) {
            LocalDate currentEndDate = currentStartDate.with(DayOfWeek.SUNDAY); // Chủ Nhật của tuần hiện tại
            if (currentEndDate.isAfter(startOfWeek)) {
                currentEndDate = startOfWeek; // Giới hạn ở tuần trước
            }

            List<Course> courses = courseRepository.findAll();

            for (Course course : courses) {
                Long enrollmentCount = enrollmentRepository.countEnrollmentsByCourseIdAndRegistrationDateBetween(
                        course.getId(), currentStartDate, currentEndDate);

                stats.add(new WeeklyEnrollmentDTO(
                        course.getId(),
                        course.getName(),
                        enrollmentCount != null ? enrollmentCount : 0,
                        currentStartDate,
                        currentEndDate
                ));
            }

            // Tiến đến tuần tiếp theo
            currentStartDate = currentStartDate.plusWeeks(1);
        }

        return stats;
    }
}

