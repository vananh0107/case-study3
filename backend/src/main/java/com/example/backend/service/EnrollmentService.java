package com.example.backend.service;

import com.example.backend.dto.CourseDTO;
import com.example.backend.dto.WeeklyEnrollmentDTO;
import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.pojo.User;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.EnrollmentRepository;
import com.example.backend.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseRepository courseRepository;

    public long countCurrentStudentsForCourse(Integer courseId) {
        return enrollmentRepository.countEnrollmentsByCourseId(courseId);
    }

    public Map<Course, Long> getWeeklyEnrollmentReport(LocalDate startDate, LocalDate endDate) {
        List<Enrollment> enrollments = enrollmentRepository.findByRegistrationDateBetweenAndActive(startDate, endDate, true);

        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getCourse, Collectors.counting()));
    }

//    public List<CourseDTO> getRegisteredCourses(String username) {
//        User student = userRepository.findByUsernameWithRoles(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
//        return enrollments.stream()
//                .map(enrollment -> modelMapper.map(enrollment.getCourse(), CourseDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    public void cancelRegistration(Integer courseId, String username) {
//        User student = userRepository.findByUsernameWithRoles(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
//        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentAndCourse(student, course);
//        if (enrollmentOpt.isPresent() && enrollmentOpt.get().getCourse().getStartDate().isAfter(LocalDate.now())) {
//            enrollmentRepository.delete(enrollmentOpt.get());
//        } else {
//            throw new RuntimeException("Cannot cancel registration");
//        }
//
//    }
//    public List<WeeklyEnrollmentDTO> getWeeklyEnrollmentStatsUntilNow() {
//        LocalDate earliestRegistrationDate = enrollmentRepository.findEarliestRegistrationDate();
//        // Nếu không có người đăng ký, ném ngoại lệ
//        if (earliestRegistrationDate == null) {
//            throw new NoSuchElementException("No registrations found.");
//        }
//
//        LocalDate today = LocalDate.now();
//        // Đặt startOfWeek là Chủ Nhật của tuần trước
//        LocalDate startOfWeek = today.with(DayOfWeek.SUNDAY).minusWeeks(1);
//
//        List<WeeklyEnrollmentDTO> stats = new ArrayList<>();
//
//        // Khởi tạo currentEndDate từ Chủ Nhật của tuần trước
//        LocalDate currentEndDate = startOfWeek;
//
//        while (currentEndDate.isAfter(earliestRegistrationDate) || currentEndDate.isEqual(earliestRegistrationDate)) {
//            // currentStartDate là thứ Hai của tuần hiện tại
//            LocalDate currentStartDate = currentEndDate.with(DayOfWeek.MONDAY);
//            if (currentStartDate.isBefore(earliestRegistrationDate)) {
//                currentStartDate = earliestRegistrationDate; // Giới hạn bắt đầu từ earliestRegistrationDate nếu vượt qua
//            }
//
//            log.info("currentStartDate: " + currentStartDate + ", currentEndDate: " + currentEndDate);
//
//            // Lấy danh sách các khóa học và tính toán số lượt đăng ký cho từng khóa học
//            List<Course> courses = courseRepository.findAll();
//            for (Course course : courses) {
//                Long enrollmentCount = enrollmentRepository.countEnrollmentsByCourseIdAndRegistrationDateBetween(
//                        course.getId(), currentStartDate, currentEndDate);
//
//                stats.add(new WeeklyEnrollmentDTO(
//                        course.getId(),
//                        course.getName(),
//                        enrollmentCount != null ? enrollmentCount : 0,
//                        currentStartDate,
//                        currentEndDate
//                ));
//            }
//
//            // Giảm currentEndDate xuống một tuần (về Chủ Nhật của tuần trước)
//            currentEndDate = currentEndDate.minusWeeks(1);
//        }
//
//        return stats;
//    }
}

