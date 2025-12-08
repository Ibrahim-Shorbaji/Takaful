package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    // Create hotel profile
    Hotel createHotel(Hotel hotel);

    // Get hotel by ID
    Optional<Hotel> getHotelById(Long id);

    // Get hotel by user ID
    Optional<Hotel> getHotelByUserId(Long userId);

    // Update hotel profile
    Hotel updateHotel(Long id, Hotel hotel);

    // Delete hotel
    void deleteHotel(Long id);

    // Get all hotels
    List<Hotel> getAllHotels();

    // Search hotels by name
    List<Hotel> searchHotelsByName(String name);

    // Search hotels by address
    List<Hotel> searchHotelsByAddress(String address);

    // Get total hotels count
    long getTotalHotelsCount();

    // Check if hotel exists for user
    boolean hotelExistsForUser(Long userId);
}
