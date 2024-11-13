package com.example.backend.controller;

import com.example.backend.dto.CourseDTO;
import com.example.backend.dto.CourseRegisterDTO;
import com.example.backend.dto.StudentDTO;
import com.example.backend.service.CourseService;
import com.example.backend.service.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
@Slf4j
@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired

    private EnrollmentService enrollmentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/teacher/courses/detail/{courseId}")
    public String getStudentsByCourse(@PathVariable("courseId") Integer courseId, Model model) {
        List<StudentDTO> students = courseService.getStudentsByCourseId(courseId);
        model.addAttribute("students", students);
        model.addAttribute("courseId", courseId);
        return "courses/detail";
    }
    @GetMapping("/teacher/courses/list")
    public String showRegisterCourseForm(
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<CourseRegisterDTO> coursePage = courseService.getAllCoursesWithEnrollmentCount(page, 8);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("role", "teacher");
        return "courses/list";
    }
    @GetMapping("/teacher/courses/create")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        return "courses/create";
    }


    @GetMapping("/teacher/courses/edit/{id}")
    public String showEditCourseForm(@PathVariable("id") Integer id, Model model) {
        CourseDTO course = courseService.getCourseById(id);
        model.addAttribute("courseId", id);
        model.addAttribute("course", course);
        return "courses/edit";
    }

    @PostMapping("/teacher/courses/edit/{id}")
    public String editCourse(@PathVariable("id") Integer id, @ModelAttribute("course") CourseDTO courseDTO, RedirectAttributes redirectAttribute) {
        courseService.updateCourse(id, courseDTO);
        redirectAttribute.addFlashAttribute("message", "Cập nhật khóa học "+courseDTO.getName()+" thành công!!!");
        return "redirect:/teacher/courses/list";
    }

    @PostMapping("/teacher/courses/delete/{id}")
    public String deleteCourse(@PathVariable Integer id, RedirectAttributes redirectAttribute) {
        courseService.deleteCourse(id);
        redirectAttribute.addFlashAttribute("message", "Xóa khóa học thành công!!!");
        return "redirect:/teacher/courses/list";
    }
    @GetMapping("/student/courses/my")
    public String getCoursesForUser(Model model, Principal principal) {
        String username = principal.getName();
        List<CourseRegisterDTO> enrolledCourses = courseService.getActiveCoursesForUser(username);
        model.addAttribute("enrollments", enrolledCourses);
        return "courses/my";
    }
    @PostMapping("/teacher/courses/add")
    public String addCourse(@ModelAttribute("course") CourseDTO courseDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-course";
        }
        try {
            courseService.addCourse(courseDTO);
            model.addAttribute("message", "Khóa học đã được thêm thành công!");
            return "redirect:/teacher/courses/list";
        } catch (Exception e) {
            model.addAttribute("error", "Đã có lỗi xảy ra khi thêm khóa học!");
            return "courses/create";
        }
    }

}