package com.takaful.foodshare.DTO;


import com.takaful.foodshare.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated // Use JwtResponse instead
public class LoginResponse {

    private String token;
    private String email;
    private Role role;
    private Long userId;
    private String message;

    public LoginResponse(String token, String email, Role role, Long userId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
        this.message = "Login successful";
    }
}
