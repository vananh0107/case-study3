package com.example.backend.service;

import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.pojo.User;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.EnrollmentRepository;
import com.example.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailReminderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EmailService emailService;

    // Gửi email nhắc nhở sinh viên chưa đăng ký khóa học nào
    public void sendReminderForStudentsNotEnrolled() {
        List<User> students = userRepository.findByRole("ROLE_STUDENT");

        for (User student : students) {
            List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
            if (enrollments.isEmpty()) {
                String subject = "Reminder: Register for a Course";
                String body = "Dear " + student.getFullName() + ",\n\n"
                        + "You have not registered for any courses yet. Please register for a course soon.\n\n"
                        + "Best regards,\nYour University";
                emailService.sendEmail(student.getUsername(), subject, body);
            }
        }
    }

    public void sendReminderForCoursesWithLowEnrollment() {
        List<Course> coursesWithLowEnrollment = courseRepository.findCoursesWithLowEnrollment();

        for (Course course : coursesWithLowEnrollment) {
            List<User> students = userRepository.findByRole("ROLE_STUDENT");
            for (User student : students) {
                String subject = "Reminder: Course " + course.getName() + " Needs More Enrollment";
                String body = "Dear " + student.getFullName() + ",\n\n"
                        + "The course " + course.getName() + " is still open for enrollment but needs more students.\n"
                        + "Please consider enrolling in this course soon.\n\n"
                        + "Best regards,\nYour University";

                emailService.sendEmail(student.getUsername(), subject, body);
            }
        }
    }

    public void sendEnrollmentReminders() {
        sendReminderForStudentsNotEnrolled();
        sendReminderForCoursesWithLowEnrollment();
    }
}