package com.example.studentmanagement.repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.studentmanagement.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT COUNT(DISTINCT s.course) FROM Student s")
    long countDistinctCourses();

    List<Student> findTop5ByOrderByIdDesc();
    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByRollNumberContainingIgnoreCase(String rollNumber);

    List<Student> findByCourseContainingIgnoreCase(String course);
    Page<Student> findAll(Pageable pageable);
    @Query("""
    	    SELECT s FROM Student s
    	    WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	       OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	       OR LOWER(s.course) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	    """)
    	Page<Student> searchStudents(
    	        @Param("keyword") String keyword,
    	        Pageable pageable
    	);
    
}