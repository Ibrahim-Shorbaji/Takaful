package com.takaful.foodshare.controller;

import com.takaful.foodshare.DTO.ApiResponse;
import com.takaful.foodshare.DTO.OrderResponse;
import com.takaful.foodshare.DTO.ReserveFoodRequest;
import com.takaful.foodshare.DTO.UpdateOrderRequest;
import com.takaful.foodshare.entity.Order;
import com.takaful.foodshare.enums.OrderStatus;
import com.takaful.foodshare.mapper.OrderMapper;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<OrderResponse>> reserveFood(
            @Valid @RequestBody ReserveFoodRequest request) {
        try {
            // Reserve food (creates order automatically)
            Order order = orderService.reserveFood(
                    request.getFoodId(),
                    request.getCharityId(),
                    request.getNotes()
            );

            OrderResponse response = orderMapper.toResponse(order);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Food reserved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

            OrderResponse response = orderMapper.toResponse(order);
            return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/charity/{charityId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByCharityId(
            @PathVariable Long charityId) {
        try {
            List<Order> orders = orderService.getOrdersByCharityId(charityId);
            List<OrderResponse> responses = orders.stream()
                    .map(orderMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/charity/{charityId}/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByCharityIdAndStatus(
            @PathVariable Long charityId,
            @PathVariable OrderStatus status) {
        try {
            List<Order> orders = orderService.getOrdersByCharityIdAndStatus(charityId, status);
            List<OrderResponse> responses = orders.stream()
                    .map(orderMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/charity/{charityId}/upcoming")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUpcomingPickupsByCharityId(
            @PathVariable Long charityId) {
        try {
            List<Order> orders = orderService.getUpcomingPickupsByCharityId(charityId);
            List<OrderResponse> responses = orders.stream()
                    .map(orderMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Upcoming pickups retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByHotelId(
            @PathVariable Long hotelId) {
        try {
            List<Order> orders = orderService.getOrdersByHotelId(hotelId);
            List<OrderResponse> responses = orders.stream()
                    .map(orderMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/hotel/{hotelId}/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByHotelIdAndStatus(
            @PathVariable Long hotelId,
            @PathVariable OrderStatus status) {
        try {
            List<Order> orders = orderService.getOrdersByHotelIdAndStatus(hotelId, status);
            List<OrderResponse> responses = orders.stream()
                    .map(orderMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/hotel/{hotelId}/upcoming")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUpcomingPickupsByHotelId(
            @PathVariable Long hotelId) {
        try {
            List<Order> orders = orderService.getUpcomingPickupsByHotelId(hotelId);
            List<OrderResponse> responses = orders.stream()
                    .map(orderMapper::toResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Upcoming pickups retrieved successfully", responses));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderRequest request) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

            order.setNotes(request.getNotes());
            Order updatedOrder = orderService.updateOrder(id, order);
            OrderResponse response = orderMapper.toResponse(updatedOrder);

            return ResponseEntity.ok(ApiResponse.success("Order updated successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/pickup")
    public ResponseEntity<ApiResponse<OrderResponse>> markAsPickedUp(@PathVariable Long id) {
        try {
            Order order = orderService.markAsPickedUp(id);
            OrderResponse response = orderMapper.toResponse(order);

            return ResponseEntity.ok(ApiResponse.success("Order marked as picked up", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        try {
            Order order = orderService.updateOrderStatus(id, status);
            OrderResponse response = orderMapper.toResponse(order);

            return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
