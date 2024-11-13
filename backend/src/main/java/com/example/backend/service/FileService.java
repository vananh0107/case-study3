package com.example.backend.service;

import com.example.backend.dto.CourseDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;;

@Service
public class FileService {
    public void reportCoursesReport(Map<CourseDTO, Long> reportData, LocalDate startDate, LocalDate endDate) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("weekly_report.csv"))) {
            writer.write(String.format("Report from %s to %s\n", startDate, endDate));
            writer.write("Course Name,Enrollment Count\n");
            for (Map.Entry<CourseDTO, Long> entry : reportData.entrySet()) {
                writer.write(String.format("%s,%d\n",
                        entry.getKey().getName(),
                        entry.getValue()));
            }
        }
        catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
