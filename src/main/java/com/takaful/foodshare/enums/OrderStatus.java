package com.takaful.foodshare.enums;

public enum OrderStatus {
    CONFIRMED,      // Auto-confirmed when charity clicks reserve
    PICKED_UP,      // Charity picked up the food
    CANCELLED       // Either party cancelled
}
