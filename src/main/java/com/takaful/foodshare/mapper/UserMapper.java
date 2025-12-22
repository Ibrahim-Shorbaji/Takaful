package com.takaful.foodshare.mapper;

import com.takaful.foodshare.DTO.UserResponse;
import com.takaful.foodshare.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Entity to Response
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
