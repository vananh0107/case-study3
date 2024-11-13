package com.example.backend.controller;

import com.example.backend.dto.CourseDTO;
import com.example.backend.service.EnrollmentService;
import com.example.backend.service.FileService;
import org.springframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ReportController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private FileService fileService;

    @GetMapping("/teacher/courses/weekly-report")
    public String getWeeklyReport(@RequestParam(value = "weekOption", required = false, defaultValue = "0") String weekOption,
                                  Model model) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<String> weekRanges = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            LocalDate weekStartDate = endDate.minusWeeks(i).with(DayOfWeek.MONDAY);
            LocalDate weekEndDate = weekStartDate.plusDays(6);
            weekRanges.add(weekStartDate.format(DateTimeFormatter.ofPattern("d/MM/yyyy")) + " - " +
                    weekEndDate.format(DateTimeFormatter.ofPattern("d/MM/yyyy")));
        }

        int selectedWeek = Integer.parseInt(weekOption);
        if (selectedWeek > 0) {
            startDate = endDate.minusWeeks(selectedWeek).with(DayOfWeek.MONDAY);
            endDate = startDate.plusDays(6);
        }

        Map<CourseDTO, Long> reportData = enrollmentService.getWeeklyEnrollmentReport(startDate, endDate);

        model.addAttribute("weekRanges", weekRanges);
        model.addAttribute("reportData", reportData);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("weekOption", weekOption);

        return "report/register";
    }

    @GetMapping("/teacher/courses/export-weekly-report")
    @ResponseBody
    public ResponseEntity<Resource> exportWeeklyReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {

        Map<CourseDTO, Long> weeklyReportData = enrollmentService.getWeeklyEnrollmentReport(startDate, endDate);

        Path path = Paths.get("weekly_report.csv");
        fileService.reportCoursesReport(weeklyReportData, startDate, endDate);

        Resource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weekly_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

}

