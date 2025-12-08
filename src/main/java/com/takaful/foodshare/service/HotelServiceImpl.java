package com.takaful.foodshare.service;


import com.takaful.foodshare.entity.Hotel;
import com.takaful.foodshare.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private  HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        if (hotelExistsForUser(hotel.getUser().getId())) {
            throw new RuntimeException("Hotel profile already exists for this user");
        }
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> getHotelByUserId(Long userId) {
        return hotelRepository.findByUserId(userId);
    }

    @Override
    public Hotel updateHotel(Long id, Hotel hotel) {
        Hotel existingHotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));

        existingHotel.setHotelName(hotel.getHotelName());
        existingHotel.setAddress(hotel.getAddress());
        existingHotel.setContactPhone(hotel.getContactPhone());
        existingHotel.setDescription(hotel.getDescription());

        return hotelRepository.save(existingHotel);
    }

    @Override
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new RuntimeException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> searchHotelsByName(String name) {
        return hotelRepository.findByHotelNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> searchHotelsByAddress(String address) {
        return hotelRepository.findByAddressContainingIgnoreCase(address);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalHotelsCount() {
        return hotelRepository.countTotalHotels();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hotelExistsForUser(Long userId) {
        return hotelRepository.findByUserId(userId).isPresent();
    }
}
