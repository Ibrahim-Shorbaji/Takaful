package com.takaful.foodshare.repository;

import com.takaful.foodshare.entity.User;
import com.takaful.foodshare.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email for authentication
    Optional<User> findByEmail(String email);

    // Check if email already exists (for registration validation)
    boolean existsByEmail(String email);

    // Find all users by role (for admin purposes)
    List<User> findByRole(Role role);

    // Find active users only
    List<User> findByIsActiveTrue();

    // Find active users by role
    List<User> findByRoleAndIsActiveTrue(Role role);
}
