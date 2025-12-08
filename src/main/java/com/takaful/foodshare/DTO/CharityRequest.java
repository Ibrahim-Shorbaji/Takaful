package com.takaful.foodshare.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharityRequest {

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    private String registrationNumber;

    private String description;
}
