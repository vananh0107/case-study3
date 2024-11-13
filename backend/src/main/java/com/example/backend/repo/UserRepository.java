package com.example.backend.repo;

import com.example.backend.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(String role);
    @Query("SELECT u FROM User u WHERE u.role = 'ROLE_STUDENT' AND u.id NOT IN (SELECT e.student.id FROM Enrollment e)")
    List<User> findStudentsNotEnrolled();
}
