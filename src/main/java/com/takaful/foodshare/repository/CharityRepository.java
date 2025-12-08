package com.takaful.foodshare.repository;

import com.takaful.foodshare.entity.Charity;
import com.takaful.foodshare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharityRepository extends JpaRepository<Charity,Long> {
    // Find charity by user
    Optional<Charity> findByUser(User user);

    // Find charity by user id
    Optional<Charity> findByUserId(Long userId);

    // Check if registration number exists (for validation)
    boolean existsByRegistrationNumber(String registrationNumber);

    // Find charity by registration number
    Optional<Charity> findByRegistrationNumber(String registrationNumber);

    // Search charities by name (case-insensitive)
    List<Charity> findByOrganizationNameContainingIgnoreCase(String organizationName);

    // Search charities by address
    List<Charity> findByAddressContainingIgnoreCase(String address);

    // Get total number of charities
    @Query("SELECT COUNT(c) FROM Charity c")
    long countTotalCharities();
}
