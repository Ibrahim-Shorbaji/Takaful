package com.takaful.foodshare.DTO;

import com.takaful.foodshare.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private Long foodId;
    private Long charityId;

    private String foodTitle;
    private Long foodQuantity;
    private LocalDateTime pickupTime;

    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;

    private String charityName;
    private String charityPhone;

    private OrderStatus status;
    private String notes;
    private LocalDateTime requestedAt;
    private LocalDateTime pickedUpAt;
private LocalDateTime updatedAt;

}
