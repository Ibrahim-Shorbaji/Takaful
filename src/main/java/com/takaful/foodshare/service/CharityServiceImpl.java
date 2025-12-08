package com.takaful.foodshare.service;

import com.takaful.foodshare.entity.Charity;
import com.takaful.foodshare.repository.CharityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CharityServiceImpl implements CharityService {

    @Autowired
    private CharityRepository charityRepository;

    @Override
    public Charity createCharity(Charity charity) {
        if (charityExistsForUser(charity.getUser().getId())) {
            throw new RuntimeException("Charity profile already exists for this user");
        }

        if (charity.getRegistrationNumber() != null &&
                registrationNumberExists(charity.getRegistrationNumber())) {
            throw new RuntimeException("Registration number already exists");
        }

        return charityRepository.save(charity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Charity> getCharityById(Long id) {
        return charityRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Charity> getCharityByUserId(Long userId) {
        return charityRepository.findByUserId(userId);
    }

    @Override
    public Charity updateCharity(Long id, Charity charity) {
        Charity existingCharity = charityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charity not found with id: " + id));

        existingCharity.setOrganizationName(charity.getOrganizationName());
        existingCharity.setAddress(charity.getAddress());
        existingCharity.setContactPhone(charity.getContactPhone());
        existingCharity.setDescription(charity.getDescription());

        if (charity.getRegistrationNumber() != null &&
                !charity.getRegistrationNumber().equals(existingCharity.getRegistrationNumber())) {
            if (registrationNumberExists(charity.getRegistrationNumber())) {
                throw new RuntimeException("Registration number already exists");
            }
            existingCharity.setRegistrationNumber(charity.getRegistrationNumber());
        }

        return charityRepository.save(existingCharity);
    }

    @Override
    public void deleteCharity(Long id) {
        if (!charityRepository.existsById(id)) {
            throw new RuntimeException("Charity not found with id: " + id);
        }
        charityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Charity> getAllCharities() {
        return charityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Charity> searchCharitiesByName(String name) {
        return charityRepository.findByOrganizationNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Charity> searchCharitiesByAddress(String address) {
        return charityRepository.findByAddressContainingIgnoreCase(address);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCharitiesCount() {
        return charityRepository.countTotalCharities();
    }

    @Override
    public boolean charityExistsForUser(Long userId) {
        return charityRepository.findByUserId(userId).isPresent();
    }

    @Override
    public boolean registrationNumberExists(String registrationNumber) {
        return charityRepository.existsByRegistrationNumber(registrationNumber);
    }

}
