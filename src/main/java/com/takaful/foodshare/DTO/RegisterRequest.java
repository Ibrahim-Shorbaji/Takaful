package com.takaful.foodshare.DTO;

import com.takaful.foodshare.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // Profile fields (based on role)
    private String hotelName;          // For HOTEL role
    private String organizationName;   // For CHARITY role

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    private String description;
    private String registrationNumber;  // For CHARITY role (optional)
}
