package com.mcp.travel.controller;

import com.mcp.travel.dto.ItineraryRequest;
import com.mcp.travel.dto.TravelRequest;
import com.mcp.travel.dto.TravelResponse;
import com.mcp.travel.service.IcsService;
import com.mcp.travel.service.McpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TravelController {
    
    private static final Logger logger = LoggerFactory.getLogger(TravelController.class);
    
    @Autowired
    private McpService mcpService;
    
    @Autowired
    private IcsService icsService;
    
    @Value("${openai.api.key:}")
    private String defaultOpenaiApiKey;
    
    @Value("${google.maps.api.key:}")
    private String defaultGoogleMapsApiKey;
    
    private Map<String, String> itineraryCache = new HashMap<>();
    private Map<String, TravelRequest> requestCache = new HashMap<>();
    private Map<String, LocalDate> startDateCache = new HashMap<>();
    
    @PostMapping("/itinerary")
    public ResponseEntity<TravelResponse> generateItinerary(@Valid @RequestBody ItineraryRequest request) {
        try {
            logger.info("Received simple itinerary request for destination: {}", request.getDestination());
            
            // Validate API keys
            if (defaultOpenaiApiKey == null || defaultOpenaiApiKey.isEmpty()) {
                logger.error("OpenAI API key is not configured");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TravelResponse(null, false, 
                        "Error: OpenAI API key is not configured. Please set OPENAI_API_KEY environment variable or configure it in application.properties"));
            }
            
            logger.debug("Using OpenAI API key: {}...", defaultOpenaiApiKey.substring(0, Math.min(7, defaultOpenaiApiKey.length())));
            
            // Use default API keys from configuration
            String openaiKey = defaultOpenaiApiKey;
            String googleMapsKey = defaultGoogleMapsApiKey != null && !defaultGoogleMapsApiKey.isEmpty() ? 
                defaultGoogleMapsApiKey : "";
            
            String itinerary = mcpService.runTravelPlanner(
                request.getDestination(),
                request.getDays(),
                "General sightseeing",
                request.getBudget(),
                openaiKey,
                googleMapsKey
            );
            
            // Cache the itinerary
            String sessionId = java.util.UUID.randomUUID().toString();
            itineraryCache.put(sessionId, itinerary);
            startDateCache.put(sessionId, LocalDate.now());
            
            // Check if Airbnb data was used
            boolean usedAirbnb = itinerary.toLowerCase().contains("airbnb") && 
                                 (itinerary.toLowerCase().contains("listing") || 
                                  itinerary.toLowerCase().contains("accommodation"));
            
            String message = usedAirbnb ? 
                "Used real Airbnb listings for accommodation recommendations" :
                "Used general knowledge for accommodation suggestions (Airbnb MCP may have failed to connect)";
            
            TravelResponse response = new TravelResponse(itinerary, usedAirbnb, message);
            
            // Add session ID to response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Session-Id", sessionId);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(response);
            
        } catch (Exception e) {
            logger.error("Error generating simple itinerary", e);
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("401")) {
                errorMessage = "OpenAI API authentication failed (401 Unauthorized). Please verify your API key is correct and active. Original error: " + errorMessage;
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new TravelResponse(null, false, "Error: " + errorMessage));
        }
    }
    
    @PostMapping("/itinerary/full")
    public ResponseEntity<TravelResponse> generateFullItinerary(@Valid @RequestBody TravelRequest request) {
        try {
            logger.info("Received full itinerary request for destination: {}", request.getDestination());
            
            String itinerary = mcpService.runTravelPlanner(
                request.getDestination(),
                request.getNumDays(),
                request.getCombinedPreferences(),
                request.getBudget(),
                request.getOpenaiApiKey(),
                request.getGoogleMapsApiKey()
            );
            
            // Cache the itinerary
            String sessionId = java.util.UUID.randomUUID().toString();
            itineraryCache.put(sessionId, itinerary);
            requestCache.put(sessionId, request);
            startDateCache.put(sessionId, request.getStartDate());
            
            // Check if Airbnb data was used
            boolean usedAirbnb = itinerary.toLowerCase().contains("airbnb") && 
                                 (itinerary.toLowerCase().contains("listing") || 
                                  itinerary.toLowerCase().contains("accommodation"));
            
            String message = usedAirbnb ? 
                "Used real Airbnb listings for accommodation recommendations" :
                "Used general knowledge for accommodation suggestions (Airbnb MCP may have failed to connect)";
            
            TravelResponse response = new TravelResponse(itinerary, usedAirbnb, message);
            
            // Add session ID to response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Session-Id", sessionId);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(response);
            
        } catch (Exception e) {
            logger.error("Error generating full itinerary", e);
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("401")) {
                errorMessage = "OpenAI API authentication failed (401 Unauthorized). Please verify your API key is correct and active. Original error: " + errorMessage;
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new TravelResponse(null, false, "Error: " + errorMessage));
        }
    }
    
    @GetMapping("/itinerary/download")
    public ResponseEntity<byte[]> downloadItinerary(@RequestParam String sessionId) {
        try {
            String itinerary = itineraryCache.get(sessionId);
            LocalDate startDate = startDateCache.get(sessionId);
            
            if (itinerary == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Use cached start date or default to today
            if (startDate == null) {
                startDate = LocalDate.now();
            }
            
            byte[] icsContent = icsService.generateIcsContent(itinerary, startDate);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/calendar"));
            headers.setContentDispositionFormData("attachment", "travel_itinerary.ics");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(icsContent);
            
        } catch (Exception e) {
            logger.error("Error downloading itinerary", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "MCP Travel Planner");
        return ResponseEntity.ok(status);
    }
}
