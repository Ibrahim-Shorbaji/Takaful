package com.takaful.foodshare.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharityResponse {
    private Long id;
    private Long userId;
    private String organizationName;
    private String address;
    private String contactPhone;
    private String registrationNumber;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Statistics (optional, for dashboard)
    private Long totalOrders;
    private Long totalCompleted;
}
