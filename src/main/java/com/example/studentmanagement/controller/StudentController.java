package com.example.studentmanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Display all students
    @GetMapping
    public String listStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 5;

        if (page < 0) {
            page = 0;
        }

        Page<Student> studentPage;

        if (keyword == null || keyword.trim().isEmpty()) {

            // Pagination without search
            studentPage = studentService.getStudentsPage(page, pageSize);

        } else {

            // Database-backed search with pagination
            studentPage = studentService.searchStudentsPage(
                    keyword.trim(), page, pageSize);
        }

        model.addAttribute("studentPage", studentPage);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());

        return "students";
    }    // Display the form to add a student
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    // Save a new student
 // Save a new or updated student
    @PostMapping("/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "student-form";
        }

        studentService.saveStudent(student);
        return "redirect:/students";
    }
    // Display the form to edit a student
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        Student student = studentService.getStudentById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid student ID: " + id));

        model.addAttribute("student", student);

        return "student-form";
    }

    // Delete a student
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}