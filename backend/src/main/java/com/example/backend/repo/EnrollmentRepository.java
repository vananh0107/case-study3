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
    int countEnrollmentsByCourseId(@Param("courseId") Integer courseId);

    List<Enrollment> findByStudent(User student);

    boolean existsByStudentAndCourse(User student, Course course);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    List<Enrollment> findByCourseAndActiveTrue(Course course);

    List<Enrollment> findByStudentAndActiveTrue(User student);

    @Query("SELECT MIN(e.registrationDate) FROM Enrollment e")
    LocalDate findEarliestRegistrationDate();

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.registrationDate BETWEEN :startDate AND :endDate")
    Long countEnrollmentsByCourseIdAndRegistrationDateBetween(@Param("courseId") Integer courseId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
