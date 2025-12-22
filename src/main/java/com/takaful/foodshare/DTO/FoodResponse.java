package com.takaful.foodshare.DTO;

import com.takaful.foodshare.enums.FoodStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FoodResponse {

    private Long id;
    private Long hotelId;
    private String hotelName;       // For display
    private String hotelAddress;    // For display
    private String hotelPhone;      // For display
    private String title;
    private String description;
    private Long quantity;
    private LocalDateTime availableUntil;
    private LocalDateTime pickupTime;
    private FoodStatus status;
    private LocalDateTime postedAt;
    private LocalDateTime updatedAt;

    private Boolean isReserved;

}
