package com.example.backend.service;

import com.example.backend.dto.CourseDTO;
import com.example.backend.dto.CourseRegisterDTO;
import com.example.backend.dto.StudentDTO;
import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.pojo.User;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.EnrollmentRepository;
import com.example.backend.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CourseDTO addCourse(CourseDTO courseDTO) {
        Course course = modelMapper.map(courseDTO, Course.class);
        Course savedCourse = courseRepository.save(course);
        return modelMapper.map(savedCourse, CourseDTO.class);
    }

    public CourseDTO updateCourse(Integer courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Khóa học với ID: " + courseId + " không tìm thấy"));

        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setMaxStudents(courseDTO.getMaxStudents());
        course.setStartDate(courseDTO.getStartDate());

        Course updatedCourse = courseRepository.save(course);
        return modelMapper.map(updatedCourse, CourseDTO.class);
    }

    public void deleteCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new NoSuchElementException("Khóa học với ID: " + courseId + " không tìm thấy");
        }
        courseRepository.deleteById(courseId);
    }

    public Page<CourseRegisterDTO> getAllCoursesWithEnrollmentCount(int page, int size) {
        return courseRepository.findAllCoursesWithEnrollmentCount(PageRequest.of(page, size));
    }

    public CourseDTO getCourseById(Integer courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NoSuchElementException("Khóa học với ID: " + courseId + " không tìm thấy"));
        return modelMapper.map(course, CourseDTO.class);
    }

    public void registerCourse(Integer courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Khóa học với ID: " + courseId + " không tìm thấy"));

        if (enrollmentRepository.countEnrollmentsByCourseId(courseId) >= course.getMaxStudents()) {
            throw new IllegalStateException("Khóa học đã đầy");
        }

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User " + username + " không tìm thấy"));

        Optional<Enrollment> existingEnrollmentOpt = enrollmentRepository.findByStudentAndCourse(student, course);

        if (existingEnrollmentOpt.isPresent()) {
            Enrollment existingEnrollment = existingEnrollmentOpt.get();
            if (!existingEnrollment.isActive()) {
                existingEnrollment.setActive(true);
                existingEnrollment.setRegistrationDate(LocalDate.now());
                enrollmentRepository.save(existingEnrollment);
            } else {
                throw new IllegalArgumentException("Bạn đã đăng kí khóa học: "+course.getName());
            }
        } else {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setRegistrationDate(LocalDate.now());
            enrollmentRepository.save(enrollment);
        }
    }

    public void unregisterCourse(Integer courseId, String username) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            if (LocalDate.now().isBefore(course.getStartDate())) {
                User student = userRepository.findByUsername(username)
                        .orElseThrow(() -> new NoSuchElementException("User " + username + " không tìm thấy"));
                Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
                        .orElseThrow(() -> new NoSuchElementException("Chưa đăng ký khóa học này"));

                enrollment.setActive(false);
                enrollmentRepository.save(enrollment);
            } else {
                throw new IllegalStateException("Không thể hủy, khóa học đã bắt đầu");
            }
        } else {
            throw new NoSuchElementException("Khóa học với ID: " + courseId + " không tìm thấy");
        }
    }
    public List<StudentDTO> getStudentsByCourseId(Integer courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Khóa học với ID: " + courseId + " không tìm thấy"));

        List<Enrollment> enrollments = enrollmentRepository.findByCourseAndActiveTrue(course);
        return enrollments.stream()
                .map(enrollment -> {
                    User student = enrollment.getStudent();
                    StudentDTO studentDTO = new StudentDTO();
                    studentDTO.setId(student.getId());
                    studentDTO.setFullName(student.getFullName());
                    studentDTO.setUsername(student.getUsername());
                    return studentDTO;
                })
                .collect(Collectors.toList());
    }

    public List<CourseRegisterDTO> getActiveCoursesForUser(String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại"));

        List<Enrollment> activeEnrollments = enrollmentRepository.findByStudentAndActiveTrue(student);
        List<CourseRegisterDTO> courseRegisterDTOs = new ArrayList<>();

        for (Enrollment enrollment : activeEnrollments) {
            Course course = enrollment.getCourse();

            Long currentStudentCount = enrollmentRepository.countEnrollmentsByCourseId(course.getId());

            CourseRegisterDTO dto = new CourseRegisterDTO(
                    course.getId(),
                    course.getName(),
                    course.getDescription(),
                    course.getMaxStudents(),
                    course.getStartDate(),
                    currentStudentCount
            );
            courseRegisterDTOs.add(dto);
        }
        return courseRegisterDTOs;
    }
}

