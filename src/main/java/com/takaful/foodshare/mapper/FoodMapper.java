package com.takaful.foodshare.mapper;

import com.takaful.foodshare.DTO.FoodRequest;
import com.takaful.foodshare.DTO.FoodResponse;
import com.takaful.foodshare.entity.Food;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {
    // Entity to Response
    public FoodResponse toResponse(Food food) {
        if (food == null) {
            return null;
        }

        FoodResponse response = new FoodResponse();
        response.setId(food.getId());
        response.setHotelId(food.getHotel() != null ? food.getHotel().getId() : null);
        response.setHotelName(food.getHotel() != null ? food.getHotel().getHotelName() : null);
        response.setHotelAddress(food.getHotel() != null ? food.getHotel().getAddress() : null);
        response.setHotelPhone(food.getHotel() != null ? food.getHotel().getContactPhone() : null);
        response.setTitle(food.getTitle());
        response.setDescription(food.getDescription());
        response.setQuantity(food.getQuantity());
        response.setAvailableUntil(food.getAvailableUntil());
        response.setStatus(food.getStatus());
        response.setPostedAt(food.getPostedAt());
        response.setUpdatedAt(food.getUpdatedAt());
        response.setIsReserved(food.getOrder() != null);

        return response;
    }

    // Request to Entity
    public Food toEntity(FoodRequest request) {
        if (request == null) {
            return null;
        }

        Food food = new Food();
        food.setTitle(request.getTitle());
        food.setDescription(request.getDescription());
        food.setQuantity(request.getQuantity());
        food.setAvailableUntil(request.getAvailableUntil());
        food.setPickupTime(request.getPickupTime());

        return food;
    }

    // Update entity from request
    public void updateEntityFromRequest(Food food, FoodRequest request) {
        if (food == null || request == null) {
            return;
        }

        food.setTitle(request.getTitle());
        food.setDescription(request.getDescription());
        food.setQuantity(request.getQuantity());
        food.setAvailableUntil(request.getAvailableUntil());
        food.setPickupTime(request.getPickupTime());
    }
}
