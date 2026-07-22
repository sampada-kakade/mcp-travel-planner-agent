package com.mcp.travel.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ItineraryRequest {
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @Min(value = 100, message = "Budget must be at least $100")
    @Max(value = 100000, message = "Budget cannot exceed $100,000")
    private int budget;
    
    @Min(value = 1, message = "Days must be at least 1")
    @Max(value = 30, message = "Days cannot exceed 30")
    private int days;
}
