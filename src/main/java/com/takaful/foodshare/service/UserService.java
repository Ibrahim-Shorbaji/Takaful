package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.User;
import com.takaful.foodshare.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // Create new user
    User createUser(User user);

    // Find user by ID
    Optional<User> getUserById(Long id);

    // Find user by email
    Optional<User> getUserByEmail(String email);

    // Update user
    User updateUser(Long id, User user);

    // Delete user (soft delete - set isActive to false)
    void deleteUser(Long id);

    // Get all users
    List<User> getAllUsers();

    // Get users by role
    List<User> getUsersByRole(Role role);

    // Get active users only
    List<User> getActiveUsers();

    // Check if email exists
    boolean emailExists(String email);

    // Activate/Deactivate user
    User toggleUserStatus(Long id);
}
