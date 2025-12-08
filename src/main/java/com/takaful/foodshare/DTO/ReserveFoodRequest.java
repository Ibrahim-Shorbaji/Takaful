package com.takaful.foodshare.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveFoodRequest {

    @NotNull(message = "food id cannot be null")
    private Long foodId;

    @NotNull(message = "charity id cannot be null")
    private Long charityId;
}
