package com.takaful.foodshare.mapper;

import com.takaful.foodshare.DTO.HotelRequest;
import com.takaful.foodshare.DTO.HotelResponse;
import com.takaful.foodshare.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {
    public HotelResponse toResponse(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setUserId(hotel.getUser() != null ? hotel.getUser().getId() : null);
        response.setHotelName(hotel.getHotelName());
        response.setAddress(hotel.getAddress());
        response.setContactPhone(hotel.getContactPhone());
        response.setDescription(hotel.getDescription());
        response.setCreatedAt(hotel.getCreatedAt());
        response.setUpdatedAt(hotel.getUpdatedAt());

        return response;
    }

    // Request to Entity
    public Hotel toEntity(HotelRequest request) {
        if (request == null) {
            return null;
        }

        Hotel hotel = new Hotel();
        hotel.setHotelName(request.getHotelName());
        hotel.setAddress(request.getAddress());
        hotel.setContactPhone(request.getContactPhone());
        hotel.setDescription(request.getDescription());

        return hotel;
    }

    // Update entity from request
    public void updateEntityFromRequest(Hotel hotel, HotelRequest request) {
        if (hotel == null || request == null) {
            return;
        }

        hotel.setHotelName(request.getHotelName());
        hotel.setAddress(request.getAddress());
        hotel.setContactPhone(request.getContactPhone());
        hotel.setDescription(request.getDescription());
    }
}
