package com.takaful.foodshare.controller;

import com.takaful.foodshare.DTO.ApiResponse;
import com.takaful.foodshare.DTO.CharityRequest;
import com.takaful.foodshare.DTO.CharityResponse;
import com.takaful.foodshare.entity.Charity;
import com.takaful.foodshare.mapper.CharityMapper;
import com.takaful.foodshare.service.CharityService;
import com.takaful.foodshare.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/charities")
@RequiredArgsConstructor
public class CharityController {

    private final CharityService charityService;
    private final OrderService orderService;
    private final CharityMapper charityMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CharityResponse>> getCharityById(@PathVariable Long id) {
        try {
            Charity charity = charityService.getCharityById(id)
                    .orElseThrow(() -> new RuntimeException("Charity not found with id: " + id));

            CharityResponse response = charityMapper.toResponse(charity);

            // Add statistics
            response.setTotalOrders(orderService.getTotalOrdersCount(id));
            response.setTotalCompleted(orderService.getCompletedOrdersCount(id));

            return ResponseEntity.ok(ApiResponse.success("Charity retrieved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CharityResponse>> getCharityByUserId(@PathVariable Long userId) {
        try {
            Charity charity = charityService.getCharityByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Charity not found for user id: " + userId));

            CharityResponse response = charityMapper.toResponse(charity);

            // Add statistics
            response.setTotalOrders(orderService.getTotalOrdersCount(charity.getId()));
            response.setTotalCompleted(orderService.getCompletedOrdersCount(charity.getId()));

            return ResponseEntity.ok(ApiResponse.success("Charity retrieved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CharityResponse>>> getAllCharities() {
        try {
            List<Charity> charities = charityService.getAllCharities();
            List<CharityResponse> responses = charities.stream()
                    .map(charityMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Charities retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CharityResponse>> updateCharity(
            @PathVariable Long id,
            @Valid @RequestBody CharityRequest request) {
        try {
            Charity charity = charityService.getCharityById(id)
                    .orElseThrow(() -> new RuntimeException("Charity not found with id: " + id));

            charityMapper.updateEntityFromRequest(charity, request);
            Charity updatedCharity = charityService.updateCharity(id, charity);
            CharityResponse response = charityMapper.toResponse(updatedCharity);

            return ResponseEntity.ok(ApiResponse.success("Charity updated successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCharity(@PathVariable Long id) {
        try {
            charityService.deleteCharity(id);
            return ResponseEntity.ok(ApiResponse.success("Charity deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CharityResponse>>> searchCharities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address) {
        try {
            List<Charity> charities;

            if (name != null && !name.isEmpty()) {
                charities = charityService.searchCharitiesByName(name);
            } else if (address != null && !address.isEmpty()) {
                charities = charityService.searchCharitiesByAddress(address);
            } else {
                charities = charityService.getAllCharities();
            }

            List<CharityResponse> responses = charities.stream()
                    .map(charityMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Search completed", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
