package com.takaful.foodshare.mapper;

import com.takaful.foodshare.DTO.CharityRequest;
import com.takaful.foodshare.DTO.CharityResponse;
import com.takaful.foodshare.entity.Charity;
import org.springframework.stereotype.Component;

@Component
public class CharityMapper {
    // Entity to Response
    public CharityResponse toResponse(Charity charity) {
        if (charity == null) {
            return null;
        }

        CharityResponse response = new CharityResponse();
        response.setId(charity.getId());
        response.setUserId(charity.getUser() != null ? charity.getUser().getId() : null);
        response.setOrganizationName(charity.getOrganizationName());
        response.setAddress(charity.getAddress());
        response.setContactPhone(charity.getContactPhone());
        response.setRegistrationNumber(charity.getRegistrationNumber());
        response.setDescription(charity.getDescription());
        response.setCreatedAt(charity.getCreatedAt());
        response.setUpdatedAt(charity.getUpdatedAt());

        return response;
    }

    // Request to Entity
    public Charity toEntity(CharityRequest request) {
        if (request == null) {
            return null;
        }

        Charity charity = new Charity();
        charity.setOrganizationName(request.getOrganizationName());
        charity.setAddress(request.getAddress());
        charity.setContactPhone(request.getContactPhone());
        charity.setRegistrationNumber(request.getRegistrationNumber());
        charity.setDescription(request.getDescription());

        return charity;
    }

    // Update entity from request
    public void updateEntityFromRequest(Charity charity, CharityRequest request) {
        if (charity == null || request == null) {
            return;
        }

        charity.setOrganizationName(request.getOrganizationName());
        charity.setAddress(request.getAddress());
        charity.setContactPhone(request.getContactPhone());
        charity.setRegistrationNumber(request.getRegistrationNumber());
        charity.setDescription(request.getDescription());
    }
}
