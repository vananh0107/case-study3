package com.example.backend.controller;

import com.example.backend.dto.CourseRegisterDTO;
import com.example.backend.service.CourseService;
import com.example.backend.service.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
public class EnrollmentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/student/courses/list")
    public String showRegisterCourseForm(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<CourseRegisterDTO> coursePage = courseService.getAllCoursesWithEnrollmentCount(page, 8);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("role", "student");
        return "courses/list";
    }
    @PostMapping("/student/courses/register")
    public String registerCourse(@RequestParam("courseId") Integer courseId,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        String username = principal.getName();  // Get logged-in user's username
        try {
            courseService.registerCourse(courseId, username);
            redirectAttributes.addFlashAttribute("message", "Register courses successful!");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("message", "Course or user not found!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", "Course is full, you can't register course!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/student/courses/register";
    }
    @PostMapping("/student/courses/unregister")
    public String unregisterCourse(@RequestParam("courseId") Integer courseId, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();

        try {
            courseService.unregisterCourse(courseId, username);
            redirectAttributes.addFlashAttribute("message", "Đã hủy đăng ký thành công.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy đăng ký, khóa học đã bắt đầu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi hủy đăng ký.");
        }

        return "redirect:/student/courses/my";
    }
}