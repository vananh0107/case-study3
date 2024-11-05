package com.example.backend.repo;

import com.example.backend.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
    @Query(value = "SELECT r.* FROM roles r " +
            "JOIN users_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = :userId", nativeQuery = true)
    List<Role> findRolesByUserId(@Param("userId") Integer userId);
}
