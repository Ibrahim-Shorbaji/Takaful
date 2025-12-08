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
    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;
    private String title;
    private String description;
    private LocalDateTime availableUntil;
    private LocalDateTime pickUpTime;
    private FoodStatus status;
    private LocalDateTime postedAt;
    private boolean isReserved;

}
