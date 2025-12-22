package com.takaful.foodshare.mapper;

import com.takaful.foodshare.DTO.OrderResponse;
import com.takaful.foodshare.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    // Entity to Response
    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setFoodId(order.getFood() != null ? order.getFood().getId() : null);
        response.setCharityId(order.getCharity() != null ? order.getCharity().getId() : null);

        // Food details
        if (order.getFood() != null) {
            response.setFoodTitle(order.getFood().getTitle());
            response.setFoodQuantity(order.getFood().getQuantity());
            response.setPickupTime(order.getFood().getPickupTime());

            // Hotel details (from food's hotel)
            if (order.getFood().getHotel() != null) {
                response.setHotelName(order.getFood().getHotel().getHotelName());
                response.setHotelAddress(order.getFood().getHotel().getAddress());
                response.setHotelPhone(order.getFood().getHotel().getContactPhone());
            }
        }

        // Charity details
        if (order.getCharity() != null) {
            response.setCharityName(order.getCharity().getOrganizationName());
            response.setCharityPhone(order.getCharity().getContactPhone());
        }

        response.setStatus(order.getStatus());
        response.setNotes(order.getNotes());
        response.setRequestedAt(order.getRequestedAt());
        response.setPickedUpAt(order.getPickedUpAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }
}
