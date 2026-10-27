package com.example.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.studentmanagement.service.StudentService;

@Controller
public class DashboardController {

    private final StudentService studentService;

    public DashboardController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String showDashboard(Model model) {

        model.addAttribute(
                "totalStudents",
                studentService.getTotalStudents()
        );

        model.addAttribute(
                "totalCourses",
                studentService.getTotalCourses()
        );

        model.addAttribute(
                "recentStudents",
                studentService.getRecentStudents()
        );
        return "dashboard";
    }
}