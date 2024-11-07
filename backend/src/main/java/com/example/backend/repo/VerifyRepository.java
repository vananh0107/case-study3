package com.example.backend.repo;

import com.example.backend.pojo.User;
import com.example.backend.pojo.Verify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyRepository extends JpaRepository<Verify, Long> {
    Verify findByUser(User user);
}
