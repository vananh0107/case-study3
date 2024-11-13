package com.example.backend.batch.CourseRegister;

import com.example.backend.dto.CourseDTO;
import com.example.backend.service.FileService;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
@Component
public class CourseRegisterWriter implements ItemWriter<Map<CourseDTO, Long>> {

    @Autowired
    private FileService fileService;
    @Override
    public void write(Chunk<? extends Map<CourseDTO, Long>> items) throws IOException {
        Map<CourseDTO, Long> reportData = items.getItems().get(0);
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusDays(6);
        fileService.reportCoursesReport(reportData,startDate, endDate);
    }
}
