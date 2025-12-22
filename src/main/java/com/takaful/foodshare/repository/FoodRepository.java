package com.takaful.foodshare.repository;

import com.takaful.foodshare.entity.Food;
import com.takaful.foodshare.entity.Hotel;
import com.takaful.foodshare.enums.FoodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    // Find all available food (for charity to browse)
    List<Food> findByStatusOrderByPostedAtDesc(FoodStatus status);


    // Find all food posted by a specific hotel
    List<Food> findByHotelOrderByPostedAtDesc(Hotel hotel);

    // Find food by hotel and status
    List<Food> findByHotelAndStatusOrderByPostedAtDesc(Hotel hotel, FoodStatus status);

    // Find food by hotel id
    List<Food> findByHotelIdOrderByPostedAtDesc(Long hotelId);

    // Find food by hotel id and status
    List<Food> findByHotelIdAndStatusOrderByPostedAtDesc(Long hotelId, FoodStatus status);

    // Find expired food (availableUntil has passed and status is still AVAILABLE)
    @Query("SELECT f FROM Food f WHERE f.availableUntil < :now AND f.status = 'AVAILABLE'")
    List<Food> findExpiredFood(@Param("now") LocalDateTime now);

    // Search available food by title or description
    @Query("SELECT f FROM Food f WHERE f.status = 'AVAILABLE' AND " +
            "(LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Food> searchAvailableFood(@Param("keyword") String keyword);

    // Count food by status for a specific hotel
    @Query("SELECT COUNT(f) FROM Food f WHERE f.hotel.id = :hotelId AND f.status = :status")
    long countByHotelIdAndStatus(@Param("hotelId") Long hotelId, @Param("status") FoodStatus status);

    // Get total food donated by hotel (COMPLETED status)
    @Query("SELECT COUNT(f) FROM Food f WHERE f.hotel.id = :hotelId AND f.status = 'COMPLETED'")
    long countCompletedByHotelId(@Param("hotelId") Long hotelId);

    // Get recent available food (last 24 hours)
    @Query("SELECT f FROM Food f WHERE f.status = 'AVAILABLE' AND f.postedAt >= :since ORDER BY f.postedAt DESC")
    List<Food> findRecentAvailableFood(@Param("since") LocalDateTime since);
}
