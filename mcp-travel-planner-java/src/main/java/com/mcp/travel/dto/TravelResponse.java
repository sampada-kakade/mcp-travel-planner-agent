package com.mcp.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TravelResponse {
    private String itinerary;
    private boolean usedAirbnbData;
    private String message;
}
