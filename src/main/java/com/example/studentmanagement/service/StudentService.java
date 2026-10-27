package com.example.studentmanagement.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Add or update a student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get a student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Delete a student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Count total students
    public long getTotalStudents() {
        return studentRepository.count();
    }
    public long getTotalCourses() {
        return studentRepository.countDistinctCourses();
    }
    public List<Student> getRecentStudents() {
        return studentRepository.findTop5ByOrderByIdDesc();
    }
    public List<Student> searchStudents(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }

        String searchText = keyword.trim();

        List<Student> studentsByName =
                studentRepository.findByNameContainingIgnoreCase(searchText);

        List<Student> studentsByRollNumber =
                studentRepository.findByRollNumberContainingIgnoreCase(searchText);

        List<Student> studentsByCourse =
                studentRepository.findByCourseContainingIgnoreCase(searchText);

        java.util.Map<Long, Student> results = new java.util.LinkedHashMap<>();

        for (Student student : studentsByName) {
            results.put(student.getId(), student);
        }

        for (Student student : studentsByRollNumber) {
            results.put(student.getId(), student);
        }

        for (Student student : studentsByCourse) {
            results.put(student.getId(), student);
        }

        return new java.util.ArrayList<>(results.values());
    }
     public Page<Student> getStudentsPage(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return studentRepository.findAll(pageable);
    }
     public Page<Student> searchStudentsPage(String keyword, int page, int size) {
    	    Pageable pageable = PageRequest.of(page, size);

    	    return studentRepository.searchStudents(keyword, pageable);
    	}
    
}