package com.example.backend.batch.EmailReminder;

import com.example.backend.pojo.User;
import com.example.backend.repo.UserRepository;
import com.example.backend.service.EmailService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EmailReminderWriter implements ItemWriter<Map<String, String>> {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(Chunk<? extends Map<String, String>> chunk) {
        String courseLowStudentSubject="Các lớp còn trống";
        String courseLowStudentBody="Các lớp sau vã chưa đủ học sinh hãy nhanh tay đăng ký" + '\n';
        for (Map<String, String> emailData : chunk) {
            String recipient = emailData.get("to");
            String subject = emailData.get("subject");
            String body = emailData.get("body");

            if (isValidEmailData(subject, body)) {
                if ("all_students".equals(recipient)) {
                    courseLowStudentBody = courseLowStudentBody + body+'\n';
                } else {
                    sendEmail(recipient, subject, body);
                }
            }
        }
        sendEmailToAllStudents(courseLowStudentSubject,courseLowStudentBody);
    }

    private boolean isValidEmailData(String subject, String body) {
        return subject != null && body != null;
    }

    private void sendEmailToAllStudents(String subject, String body) {
        List<User> students = userRepository.findByRole("ROLE_STUDENT");
        for (User student : students) {
            String personalizedBody = "Kính gửi " + student.getFullName() + ",\n\n" + body;
            sendEmail(student.getUsername(), subject, personalizedBody);
        }
    }

    private void sendEmail(String to, String subject, String body) {
        if (to != null) {
            emailService.sendEmail(to, subject, body);
        } else {
            log.warn("Không thể gửi email vì thiếu địa chỉ email.");
        }
    }
}
