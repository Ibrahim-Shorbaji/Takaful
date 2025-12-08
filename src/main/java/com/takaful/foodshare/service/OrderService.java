package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Order;
import com.takaful.foodshare.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    // Create order (Charity reserves food)
    Order createOrder(Order order);

    // Reserve food (business logic: create order + update food status)
    Order reserveFood(Long foodId, Long charityId, String notes);

    // Get order by ID
    Optional<Order> getOrderById(Long id);

    // Get order by food ID
    Optional<Order> getOrderByFoodId(Long foodId);

    // Update order
    Order updateOrder(Long id, Order order);

    // Update order status
    Order updateOrderStatus(Long orderId, OrderStatus status);

    // Mark order as picked up
    Order markAsPickedUp(Long orderId);

    // Cancel order
    void cancelOrder(Long orderId);

    // Get all orders by charity ID
    List<Order> getOrdersByCharityId(Long charityId);

    // Get orders by charity ID and status
    List<Order> getOrdersByCharityIdAndStatus(Long charityId, OrderStatus status);

    // Get upcoming pickups for charity
    List<Order> getUpcomingPickupsByCharityId(Long charityId);

    // Get all orders for hotel's food
    List<Order> getOrdersByHotelId(Long hotelId);

    // Get orders for hotel's food by status
    List<Order> getOrdersByHotelIdAndStatus(Long hotelId, OrderStatus status);

    // Get upcoming pickups for hotel
    List<Order> getUpcomingPickupsByHotelId(Long hotelId);

    // Get completed orders count for charity
    long getCompletedOrdersCount(Long charityId);

    // Get total orders count for charity
    long getTotalOrdersCount(Long charityId);

    // Check if food is already reserved
    boolean isFoodReserved(Long foodId);
}
