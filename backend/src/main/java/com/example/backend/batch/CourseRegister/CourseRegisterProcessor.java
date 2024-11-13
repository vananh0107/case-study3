package com.example.backend.batch.CourseRegister;

import com.example.backend.dto.CourseDTO;
import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class CourseRegisterProcessor implements ItemProcessor<DataReader, Map<CourseDTO, Long>> {
    @Autowired
    private  ModelMapper modelMapper;

    @Override
    public Map<CourseDTO, Long> process(DataReader data) {
        Map<Course, Long> enrollmentCountMap = data.getEnrollments().stream()
                .collect(Collectors.groupingBy(Enrollment::getCourse, Collectors.counting()));

        Map<CourseDTO, Long> reportData = new HashMap<>();
        for (Course course : data.getAllCourses()) {
            CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
            reportData.put(courseDTO, enrollmentCountMap.getOrDefault(course, 0L));
        }
        return reportData;
    }
}
