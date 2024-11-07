package com.example.backend.repo;

import com.example.backend.dto.CourseRegisterDTO;
import com.example.backend.pojo.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Page<Course> findAll(Pageable pageable);
    @Query("SELECT new com.example.backend.dto.CourseRegisterDTO(c.id, c.name, c.description, c.maxStudents, c.startDate, COUNT(e.id)) " +
            "FROM Course c " +
            "LEFT JOIN Enrollment e ON c.id = e.course.id AND e.active = true " +
            "GROUP BY c.id, c.name, c.description, c.maxStudents, c.startDate " +
            "ORDER BY c.startDate")
    Page<CourseRegisterDTO> findAllCoursesWithEnrollmentCount(Pageable pageable);
    @Query("SELECT c FROM Course c WHERE c.maxStudents > (SELECT COUNT(e) FROM Enrollment e WHERE e.course = c AND e.active = true)")
    List<Course> findCoursesWithLowEnrollment();
}
