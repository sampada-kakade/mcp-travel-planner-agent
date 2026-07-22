package com.mcp.travel.controller;

import com.mcp.travel.dto.DestinationResponse;
import com.mcp.travel.service.DestinationService;
import com.mcp.travel.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DestinationController {

    private final DestinationService destinationService;
    private final RecommendationService recommendationService;

    public DestinationController(DestinationService destinationService, RecommendationService recommendationService) {
        this.destinationService = destinationService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<DestinationResponse>> getDestinations() {
        List<DestinationResponse> response = destinationService.getDestinations().stream()
            .map(destination -> new DestinationResponse(destination.getName(), destination.getCountry(), destination.getRating()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<DestinationResponse>> getRecommendations() {
        List<DestinationResponse> response = recommendationService.getRecommendations(destinationService.getDestinations()).stream()
            .map(destination -> new DestinationResponse(destination.getName(), destination.getCountry(), destination.getRating()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
