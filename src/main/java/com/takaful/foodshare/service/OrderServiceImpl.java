package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Charity;
import com.takaful.foodshare.entity.Food;
import com.takaful.foodshare.entity.Order;
import com.takaful.foodshare.enums.FoodStatus;
import com.takaful.foodshare.enums.OrderStatus;
import com.takaful.foodshare.repository.CharityRepository;
import com.takaful.foodshare.repository.FoodRepository;
import com.takaful.foodshare.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private  FoodRepository foodRepository;
    @Autowired
    private  CharityRepository charityRepository;

    @Override
    public Order createOrder(Order order) {
        // Validate food is available
        if (isFoodReserved(order.getFood().getId())) {
            throw new RuntimeException("Food is already reserved");
        }

        Food food = foodRepository.findById(order.getFood().getId())
                .orElseThrow(() -> new RuntimeException("Food not found"));

        if (food.getStatus() != FoodStatus.AVAILABLE) {
            throw new RuntimeException("Food is not available");
        }

        // Set order status to CONFIRMED (instant reservation)
        order.setStatus(OrderStatus.CONFIRMED);

        // Update food status to RESERVED
        food.setStatus(FoodStatus.RESERVED);
        foodRepository.save(food);

        return orderRepository.save(order);
    }

    @Override
    public Order reserveFood(Long foodId, Long charityId, String notes) {
        // Validate food exists and is available
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));

        if (food.getStatus() != FoodStatus.AVAILABLE) {
            throw new RuntimeException("Food is not available for reservation");
        }

        if (isFoodReserved(foodId)) {
            throw new RuntimeException("Food is already reserved");
        }

        // Validate charity exists
        Charity charity = charityRepository.findById(charityId)
                .orElseThrow(() -> new RuntimeException("Charity not found with id: " + charityId));

        // Create new order
        Order order = new Order();
        order.setFood(food);
        order.setCharity(charity);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setNotes(notes);

        // Update food status to RESERVED
        food.setStatus(FoodStatus.RESERVED);
        foodRepository.save(food);

        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderByFoodId(Long foodId) {
        return orderRepository.findByFoodId(foodId);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Only allow updating notes if order is still CONFIRMED
        if (existingOrder.getStatus() == OrderStatus.CONFIRMED) {
            existingOrder.setNotes(order.getNotes());
        }

        return orderRepository.save(existingOrder);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status);

        // Update food status based on order status
        Food food = order.getFood();
        if (status == OrderStatus.PICKED_UP) {
            food.setStatus(FoodStatus.COMPLETED);
            order.setPickedUpAt(LocalDateTime.now());
        } else if (status == OrderStatus.CANCELLED) {
            food.setStatus(FoodStatus.AVAILABLE);
        }

        foodRepository.save(food);
        return orderRepository.save(order);
    }

    @Override
    public Order markAsPickedUp(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Can only mark CONFIRMED orders as picked up");
        }

        order.setStatus(OrderStatus.PICKED_UP);
        order.setPickedUpAt(LocalDateTime.now());

        // Update food status to COMPLETED
        Food food = order.getFood();
        food.setStatus(FoodStatus.COMPLETED);
        foodRepository.save(food);

        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.PICKED_UP) {
            throw new RuntimeException("Cannot cancel an order that has been picked up");
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Make food available again
        Food food = order.getFood();
        food.setStatus(FoodStatus.AVAILABLE);
        foodRepository.save(food);

        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCharityId(Long charityId) {
        return orderRepository.findByCharityIdOrderByRequestedAtDesc(charityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByCharityIdAndStatus(Long charityId, OrderStatus status) {
        return orderRepository.findByCharityIdAndStatusOrderByRequestedAtDesc(charityId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getUpcomingPickupsByCharityId(Long charityId) {
        return orderRepository.findUpcomingPickupsByCharityId(charityId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByHotelId(Long hotelId) {
        return orderRepository.findByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByHotelIdAndStatus(Long hotelId, OrderStatus status) {
        return orderRepository.findByHotelIdAndStatus(hotelId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getUpcomingPickupsByHotelId(Long hotelId) {
        return orderRepository.findUpcomingPickupsByHotelId(hotelId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public long getCompletedOrdersCount(Long charityId) {
        return orderRepository.countCompletedByCharityId(charityId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalOrdersCount(Long charityId) {
        return orderRepository.countTotalByCharityId(charityId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFoodReserved(Long foodId) {
        return orderRepository.findByFoodId(foodId).isPresent();
    }
}
