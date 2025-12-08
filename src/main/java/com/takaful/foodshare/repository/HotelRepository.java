package com.takaful.foodshare.repository;

import com.takaful.foodshare.entity.Hotel;
import com.takaful.foodshare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    // Find hotel by user
    Optional<Hotel> findByUser(User user);

    // Find hotel by user id
    Optional<Hotel> findByUserId(Long userId);

    // Search hotels by name (case-insensitive)
    List<Hotel> findByHotelNameContainingIgnoreCase(String hotelName);

    // Search hotels by address
    List<Hotel> findByAddressContainingIgnoreCase(String address);

    // Get total number of hotels
    @Query("SELECT COUNT(h) FROM Hotel h")
    long countTotalHotels();}
