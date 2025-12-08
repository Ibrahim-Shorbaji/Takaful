package com.takaful.foodshare.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private Long userId;
    private String hotelName;
    private String address;
    private String contactPhone;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long totalFoodPosted;
    private Long totalCompleted;
}
