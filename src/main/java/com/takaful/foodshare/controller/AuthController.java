package com.takaful.foodshare.controller;


import com.takaful.foodshare.DTO.*;
import com.takaful.foodshare.entity.Charity;
import com.takaful.foodshare.entity.Hotel;
import com.takaful.foodshare.entity.User;
import com.takaful.foodshare.enums.Role;
import com.takaful.foodshare.mapper.UserMapper;
import com.takaful.foodshare.security.jwt.JwtUtils;
import com.takaful.foodshare.security.services.UserDetailsImpl;
import com.takaful.foodshare.security.services.UserDetailsServiceImpl;
import com.takaful.foodshare.service.CharityService;
import com.takaful.foodshare.service.HotelService;
import com.takaful.foodshare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;
    private final HotelService hotelService;
    private final CharityService charityService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            // Check if email already exists
            if (userService.emailExists(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Email is already in use!"));
            }

            // Create user with hashed password
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());

            User savedUser = userService.createUser(user);

            // Create profile based on role
            if (request.getRole() == Role.HOTEL) {
                Hotel hotel = new Hotel();
                hotel.setUser(savedUser);
                hotel.setHotelName(request.getHotelName());
                hotel.setAddress(request.getAddress());
                hotel.setContactPhone(request.getContactPhone());
                hotel.setDescription(request.getDescription());
                hotelService.createHotel(hotel);

            } else if (request.getRole() == Role.CHARITY) {
                Charity charity = new Charity();
                charity.setUser(savedUser);
                charity.setOrganizationName(request.getOrganizationName());
                charity.setAddress(request.getAddress());
                charity.setContactPhone(request.getContactPhone());
                charity.setRegistrationNumber(request.getRegistrationNumber());
                charity.setDescription(request.getDescription());
                charityService.createCharity(charity);
            }

            UserResponse response = userMapper.toResponse(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully!", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT tokens
            String accessToken = jwtUtils.generateJwtToken(authentication);

            // Get user details
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            JwtResponse response = new JwtResponse(
                    accessToken,
                    userDetails.getId(),
                    userDetails.getEmail(),
                    roles
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid email or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
}
