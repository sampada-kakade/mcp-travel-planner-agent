package com.mcp.travel.dto;

import javax.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelRequest {
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @Min(value = 1, message = "Number of days must be at least 1")
    @Max(value = 30, message = "Number of days cannot exceed 30")
    private int numDays;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @Min(value = 100, message = "Budget must be at least $100")
    @Max(value = 100000, message = "Budget cannot exceed $100,000")
    private int budget;
    
    private String preferences;
    
    private List<String> quickPreferences;
    
    @NotBlank(message = "OpenAI API key is required")
    private String openaiApiKey;
    
    @NotBlank(message = "Google Maps API key is required")
    private String googleMapsApiKey;
    
    public String getCombinedPreferences() {
        StringBuilder combined = new StringBuilder();
        if (preferences != null && !preferences.isEmpty()) {
            combined.append(preferences);
        }
        if (quickPreferences != null && !quickPreferences.isEmpty()) {
            if (combined.length() > 0) {
                combined.append(", ");
            }
            combined.append(String.join(", ", quickPreferences));
        }
        return combined.length() > 0 ? combined.toString() : "General sightseeing";
    }
}
