package com.example.backend.repo;

import com.example.backend.pojo.Course;
import com.example.backend.pojo.Enrollment;
import com.example.backend.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.active = true")
    Long countEnrollmentsByCourseId(@Param("courseId") Integer courseId);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    List<Enrollment> findByCourseAndActiveTrue(Course course);

    List<Enrollment> findByStudentAndActiveTrue(User student);

    List<Enrollment> findByRegistrationDateBetweenAndActive(LocalDate startDate, LocalDate endDate, boolean active);

    List<Enrollment> findByStudent(User student);
}
