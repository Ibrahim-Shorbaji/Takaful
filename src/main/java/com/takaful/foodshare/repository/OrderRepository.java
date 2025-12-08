package com.takaful.foodshare.repository;

import com.takaful.foodshare.entity.Charity;
import com.takaful.foodshare.entity.Food;
import com.takaful.foodshare.entity.Order;
import com.takaful.foodshare.enums.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find order by food (one food = one order)
    Optional<Order> findByFood(Food food);

    // Find order by food id
    Optional<Order> findByFoodId(Long foodId);

    // Find all orders by charity
    List<Order> findByCharityOrderByRequestedAtDesc(Charity charity);

    // Find orders by charity id
    List<Order> findByCharityIdOrderByRequestedAtDesc(Long charityId);

    // Find orders by charity and status
    List<Order> findByCharityAndStatusOrderByRequestedAtDesc(Charity charity, OrderStatus status);

    // Find orders by charity id and status
    List<Order> findByCharityIdAndStatusOrderByRequestedAtDesc(Long charityId, OrderStatus status);

    // Find all orders for a specific hotel's food
    @Query("SELECT o FROM Order o WHERE o.food.hotel.id = :hotelId ORDER BY o.requestedAt DESC")
    List<Order> findByHotelId(@Param("hotelId") Long hotelId);

    // Find orders for hotel's food by status
    @Query("SELECT o FROM Order o WHERE o.food.hotel.id = :hotelId AND o.status = :status ORDER BY o.requestedAt DESC")
    List<Order> findByHotelIdAndStatus(@Param("hotelId") Long hotelId, @Param("status") OrderStatus status);

    // Find upcoming pickups for charity (CONFIRMED orders with future pickup time)
    @Query("SELECT o FROM Order o WHERE o.charity.id = :charityId AND o.status = 'CONFIRMED' " +
            "AND o.food.pickupTime > :now ORDER BY o.food.pickupTime ASC")
    List<Order> findUpcomingPickupsByCharityId(@Param("charityId") Long charityId, @Param("now") LocalDateTime now);

    // Find upcoming pickups for hotel (CONFIRMED orders with future pickup time)
    @Query("SELECT o FROM Order o WHERE o.food.hotel.id = :hotelId AND o.status = 'CONFIRMED' " +
            "AND o.food.pickupTime > :now ORDER BY o.food.pickupTime ASC")
    List<Order> findUpcomingPickupsByHotelId(@Param("hotelId") Long hotelId, @Param("now") LocalDateTime now);

    // Count completed orders by charity
    @Query("SELECT COUNT(o) FROM Order o WHERE o.charity.id = :charityId AND o.status = 'PICKED_UP'")
    long countCompletedByCharityId(@Param("charityId") Long charityId);

    // Count total orders by charity
    @Query("SELECT COUNT(o) FROM Order o WHERE o.charity.id = :charityId")
    long countTotalByCharityId(@Param("charityId") Long charityId);

    // Get orders that need pickup reminder (pickup in next 2 hours, still CONFIRMED)
    @Query("SELECT o FROM Order o WHERE o.status = 'CONFIRMED' " +
            "AND o.food.pickupTime BETWEEN :now AND :twoHoursLater")
    List<Order> findOrdersNeedingReminder(@Param("now") LocalDateTime now,
                                          @Param("twoHoursLater") LocalDateTime twoHoursLater);
}