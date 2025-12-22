package com.takaful.foodshare.controller;

import com.takaful.foodshare.DTO.ApiResponse;
import com.takaful.foodshare.DTO.HotelRequest;
import com.takaful.foodshare.DTO.HotelResponse;
import com.takaful.foodshare.entity.Hotel;
import com.takaful.foodshare.mapper.HotelMapper;
import com.takaful.foodshare.service.FoodService;
import com.takaful.foodshare.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final FoodService foodService;
    private final HotelMapper hotelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelResponse>> getHotelById(@PathVariable Long id) {
        try {
            Hotel hotel = hotelService.getHotelById(id)
                    .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));

            HotelResponse response = hotelMapper.toResponse(hotel);

            // Add statistics
            response.setTotalFoodPosted((long) foodService.getFoodByHotelId(id).size());
            response.setTotalCompleted(foodService.getCompletedDonationsCount(id));

            return ResponseEntity.ok(ApiResponse.success("Hotel retrieved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<HotelResponse>> getHotelByUserId(@PathVariable Long userId) {
        try {
            Hotel hotel = hotelService.getHotelByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Hotel not found for user id: " + userId));

            HotelResponse response = hotelMapper.toResponse(hotel);

            // Add statistics
            response.setTotalFoodPosted((long) foodService.getFoodByHotelId(hotel.getId()).size());
            response.setTotalCompleted(foodService.getCompletedDonationsCount(hotel.getId()));

            return ResponseEntity.ok(ApiResponse.success("Hotel retrieved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelResponse>>> getAllHotels() {
        try {
            List<Hotel> hotels = hotelService.getAllHotels();
            List<HotelResponse> responses = hotels.stream()
                    .map(hotelMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Hotels retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelResponse>> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequest request) {
        try {
            Hotel hotel = hotelService.getHotelById(id)
                    .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));

            hotelMapper.updateEntityFromRequest(hotel, request);
            Hotel updatedHotel = hotelService.updateHotel(id, hotel);
            HotelResponse response = hotelMapper.toResponse(updatedHotel);

            return ResponseEntity.ok(ApiResponse.success("Hotel updated successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable Long id) {
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.ok(ApiResponse.success("Hotel deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HotelResponse>>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address) {
        try {
            List<Hotel> hotels;

            if (name != null && !name.isEmpty()) {
                hotels = hotelService.searchHotelsByName(name);
            } else if (address != null && !address.isEmpty()) {
                hotels = hotelService.searchHotelsByAddress(address);
            } else {
                hotels = hotelService.getAllHotels();
            }

            List<HotelResponse> responses = hotels.stream()
                    .map(hotelMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Search completed", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
