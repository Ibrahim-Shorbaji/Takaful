package com.takaful.foodshare.controller;

import com.takaful.foodshare.DTO.ApiResponse;
import com.takaful.foodshare.DTO.FoodRequest;
import com.takaful.foodshare.DTO.FoodResponse;
import com.takaful.foodshare.entity.Food;
import com.takaful.foodshare.entity.Hotel;
import com.takaful.foodshare.enums.FoodStatus;
import com.takaful.foodshare.mapper.FoodMapper;
import com.takaful.foodshare.service.FoodServiceImpl;
import com.takaful.foodshare.service.HotelServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    @Autowired
    private FoodServiceImpl foodService;
    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private HotelServiceImpl hotelService;

    @PostMapping
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(
            @Valid @RequestBody FoodRequest request,
            @RequestParam Long hotelId) {
        try {
            // Get hotel
            Hotel hotel = hotelService.getHotelById(hotelId)
                    .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

            // Convert request to entity
            Food food = foodMapper.toEntity(request);
            food.setHotel(hotel);

            // Save
            Food savedFood = foodService.createFood(food);
            FoodResponse response = foodMapper.toResponse(savedFood);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Food posted successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFoodById(@PathVariable Long id) {
        try {
            Food food = foodService.getFoodById(id)
                    .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

            FoodResponse response = foodMapper.toResponse(food);
            return ResponseEntity.ok(ApiResponse.success("Food retrieved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getAllAvailableFood() {
        try {
            List<Food> foods = foodService.getAllAvailableFood();
            List<FoodResponse> responses = foods.stream()
                    .map(foodMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Available food retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getRecentFood() {
        try {
            List<Food> foods = foodService.getRecentAvailableFood();
            List<FoodResponse> responses = foods.stream()
                    .map(foodMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Recent food retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getFoodByHotelId(@PathVariable Long hotelId) {
        try {
            List<Food> foods = foodService.getFoodByHotelId(hotelId);
            List<FoodResponse> responses = foods.stream()
                    .map(foodMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Hotel's food retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/hotel/{hotelId}/status/{status}")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getFoodByHotelIdAndStatus(
            @PathVariable Long hotelId,
            @PathVariable FoodStatus status) {
        try {
            List<Food> foods = foodService.getFoodByHotelIdAndStatus(hotelId, status);
            List<FoodResponse> responses = foods.stream()
                    .map(foodMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Food retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FoodResponse>>> searchFood(@RequestParam String keyword) {
        try {
            List<Food> foods = foodService.searchAvailableFood(keyword);
            List<FoodResponse> responses = foods.stream()
                    .map(foodMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Search completed", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodResponse>> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody FoodRequest request) {
        try {
            Food food = foodService.getFoodById(id)
                    .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

            foodMapper.updateEntityFromRequest(food, request);
            Food updatedFood = foodService.updateFood(id, food);
            FoodResponse response = foodMapper.toResponse(updatedFood);

            return ResponseEntity.ok(ApiResponse.success("Food updated successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
            return ResponseEntity.ok(ApiResponse.success("Food cancelled successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<FoodResponse>> updateFoodStatus(
            @PathVariable Long id,
            @RequestParam FoodStatus status) {
        try {
            Food food = foodService.updateFoodStatus(id, status);
            FoodResponse response = foodMapper.toResponse(food);

            return ResponseEntity.ok(ApiResponse.success("Food status updated successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
