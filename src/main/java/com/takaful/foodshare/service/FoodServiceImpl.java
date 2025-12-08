package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Food;
import com.takaful.foodshare.enums.FoodStatus;
import com.takaful.foodshare.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public Food createFood(Food food) {
        // Validate pickup time is in the future
        if (food.getPickupTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Pickup time must be in the future");
        }

        // Validate availableUntil is after pickup time
        if (food.getAvailableUntil().isBefore(food.getPickupTime())) {
            throw new RuntimeException("Available until must be after pickup time");
        }

        food.setStatus(FoodStatus.AVAILABLE);
        return foodRepository.save(food);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }

    @Override
    public Food updateFood(Long id, Food food) {
        Food existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        // Only allow update if food is still AVAILABLE
        if (existingFood.getStatus() != FoodStatus.AVAILABLE) {
            throw new RuntimeException("Cannot update food that is not available");
        }

        existingFood.setTitle(food.getTitle());
        existingFood.setDescription(food.getDescription());
        existingFood.setQuantity(food.getQuantity());
        existingFood.setAvailableUntil(food.getAvailableUntil());
        existingFood.setPickupTime(food.getPickupTime());

        return foodRepository.save(existingFood);
    }

    @Override
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        // Only allow deletion if food is AVAILABLE (not reserved)
        if (food.getStatus() == FoodStatus.RESERVED) {
            throw new RuntimeException("Cannot delete food that is already reserved");
        }

        // Mark as cancelled instead of hard delete
        food.setStatus(FoodStatus.CANCELLED);
        foodRepository.save(food);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> getAllAvailableFood() {
        return foodRepository.findByStatusOrderByPostedAtDesc(FoodStatus.AVAILABLE);
    }



    @Override
    @Transactional(readOnly = true)
    public List<Food> searchAvailableFood(String keyword) {
        return foodRepository.searchAvailableFood(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> getRecentAvailableFood() {
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        return foodRepository.findRecentAvailableFood(since);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> getFoodByHotelId(Long hotelId) {
        return foodRepository.findByHotelIdOrderByPostedAtDesc(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> getFoodByHotelIdAndStatus(Long hotelId, FoodStatus status) {
        return foodRepository.findByHotelIdAndStatusOrderByPostedAtDesc(hotelId, status);
    }

    @Override
    public Food updateFoodStatus(Long foodId, FoodStatus status) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));

        food.setStatus(status);
        return foodRepository.save(food);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCompletedDonationsCount(Long hotelId) {
        return foodRepository.countCompletedByHotelId(hotelId);
    }

    @Override
    public void markExpiredFood() {
        List<Food> expiredFood = foodRepository.findExpiredFood(LocalDateTime.now());
        expiredFood.forEach(food -> {
            food.setStatus(FoodStatus.EXPIRED);
            foodRepository.save(food);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFoodAvailable(Long foodId) {
        return foodRepository.findById(foodId)
                .map(food -> food.getStatus() == FoodStatus.AVAILABLE)
                .orElse(false);
    }
}
