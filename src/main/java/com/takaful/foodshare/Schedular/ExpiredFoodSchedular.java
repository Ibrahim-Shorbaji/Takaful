package com.takaful.foodshare.Schedular;


import com.takaful.foodshare.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpiredFoodSchedular {

    private static final Logger logger = LoggerFactory.getLogger(ExpiredFoodSchedular.class);

    private final FoodService foodService;

    /**
     * Mark expired food every hour
     * Runs at: 00:00, 01:00, 02:00, ... 23:00 every day
     */
    @Scheduled(cron = "0 * * * * *")  // Every minute
    public void markExpiredFood() {
        logger.info("Running scheduled task: Marking expired food...");

        try {
            foodService.markExpiredFood();
            logger.info("Successfully marked expired food");
        } catch (Exception e) {
            logger.error("Error marking expired food: {}", e.getMessage());
        }
    }
}
