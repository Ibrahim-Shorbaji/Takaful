package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Food;
import com.takaful.foodshare.enums.FoodStatus;

import java.util.List;
import java.util.Optional;

public interface FoodService {

    // Create new food donation (Hotel posts food)
    Food createFood(Food food);

    // Get food by ID
    Optional<Food> getFoodById(Long id);

    // Update food
    Food updateFood(Long id, Food food);

    // Delete food (Cancel donation)
    void deleteFood(Long id);

    // Get all available food (for charity to browse)
    List<Food> getAllAvailableFood();



    // Search available food by keyword
    List<Food> searchAvailableFood(String keyword);

    // Get recent available food (last 24 hours)
    List<Food> getRecentAvailableFood();

    // Get all food by hotel ID
    List<Food> getFoodByHotelId(Long hotelId);

    // Get food by hotel ID and status
    List<Food> getFoodByHotelIdAndStatus(Long hotelId, FoodStatus status);

    // Update food status
    Food updateFoodStatus(Long foodId, FoodStatus status);

    // Get completed donations count for hotel
    long getCompletedDonationsCount(Long hotelId);

    // Mark expired food (scheduled task)
    void markExpiredFood();

    // Check if food is available
    boolean isFoodAvailable(Long foodId);
}
