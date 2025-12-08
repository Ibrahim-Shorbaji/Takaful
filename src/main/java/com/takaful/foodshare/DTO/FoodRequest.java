package com.takaful.foodshare.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class FoodRequest {

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Quantity cannot be null")
    private Long quantity;

    @NotNull(message = "Available until cannot be null")
    @Future(message = "Available until must be a future date and time")
    private LocalDateTime availableUntil;

    @NotNull(message = "Pickup time cannot be null")
    @Future(message = "Pickup time must be a future date and time")
    private LocalDateTime pickupTime;
}
