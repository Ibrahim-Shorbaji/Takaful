package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Charity;

import java.util.List;
import java.util.Optional;

public interface CharityService {
    // Create charity profile
    Charity createCharity(Charity charity);

    // Get charity by ID
    Optional<Charity> getCharityById(Long id);

    // Get charity by user ID
    Optional<Charity> getCharityByUserId(Long userId);

    // Update charity profile
    Charity updateCharity(Long id, Charity charity);

    // Delete charity
    void deleteCharity(Long id);

    // Get all charities
    List<Charity> getAllCharities();

    // Search charities by name
    List<Charity> searchCharitiesByName(String name);

    // Search charities by address
    List<Charity> searchCharitiesByAddress(String address);

    // Get total charities count
    long getTotalCharitiesCount();

    // Check if charity exists for user
    boolean charityExistsForUser(Long userId);

    // Check if registration number exists
    boolean registrationNumberExists(String registrationNumber);
}
