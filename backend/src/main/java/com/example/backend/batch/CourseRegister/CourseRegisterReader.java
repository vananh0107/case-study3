package com.example.backend.batch.CourseRegister;

import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.repo.CourseRepository;
import com.example.backend.repo.EnrollmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@Component
public class CourseRegisterReader implements ItemReader<DataReader> {

    @Autowired
    private  CourseRepository courseRepository;

    @Autowired
    private  EnrollmentRepository enrollmentRepository;

    private boolean hasRead = false;

    @Override
    public DataReader read() {
        if (!hasRead) {
            LocalDate endDate = LocalDate.now().minusDays(1);
            LocalDate startDate = endDate.minusDays(6);
            hasRead = true;

            List<Course> courses = courseRepository.findAll();
            List<Enrollment> enrollments = enrollmentRepository.findByRegistrationDateBetweenAndActive(startDate, endDate, true);
            return new DataReader(startDate, endDate, courses, enrollments);
        }
        return null;
    }
}
