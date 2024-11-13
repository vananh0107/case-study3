package com.example.backend.batch.EmailReminder;

import com.example.backend.pojo.Course;
import com.example.backend.pojo.User;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class EmailReminderReader implements ItemReader<Object> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private List<Object> reminders = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    public Object read() {
        if (reminders.isEmpty()) {
            List<User> studentsNotEnrolled = userRepository.findStudentsNotEnrolled();
            log.info("Students not enrolled: " + studentsNotEnrolled.size());
            reminders.addAll(studentsNotEnrolled);

            List<Course> coursesWithLowEnrollment = courseRepository.findCoursesWithLowEnrollment();
            log.info("Courses with low enrollment: " + coursesWithLowEnrollment.size());
            reminders.addAll(coursesWithLowEnrollment);
        }

        if (currentIndex < reminders.size()) {
            return reminders.get(currentIndex++);
        } else {
            return null;
        }
    }
}
