package com.example.backend.config;

import com.example.backend.dto.CourseRegisterDTO;
import com.example.backend.repo.CourseRepository;
import com.example.backend.service.EmailReminderService;
import jakarta.transaction.Transactional;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@EnableScheduling
@Configuration
public class BatchScheduler {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EmailReminderService emailReminderService;
    @Scheduled(cron = "0 0 9 ? * MON")
    @Transactional
    public void generateWeeklyReport() throws IOException {
        List<CourseRegisterDTO> courses = courseRepository.findAllCoursesWithEnrollmentCount(Pageable.unpaged()).getContent();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("weekly_report.csv"))) {
            writer.write("Course Name,Description,Max Students,Start Date,Enrollment Count\n");
            for (CourseRegisterDTO course : courses) {
                writer.write(String.format("%s,%s,%d,%s,%d\n",
                        course.getName(),
                        course.getDescription(),
                        course.getMaxStudents(),
                        course.getStartDate(),
                        course.getCurrentStudentCount()));
            }
        }
    }
    @Scheduled(cron = "0 0 9 ? * MON")
    public void sendWeeklyReminder() {
        emailReminderService.sendEnrollmentReminders();
    }
}

