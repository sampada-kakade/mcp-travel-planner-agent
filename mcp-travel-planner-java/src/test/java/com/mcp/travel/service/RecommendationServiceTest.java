package com.mcp.travel.service;

import com.mcp.travel.domain.Destination;
import com.mcp.travel.service.impl.DefaultRecommendationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationServiceTest {

    private final RecommendationService recommendationService = new DefaultRecommendationService();

    @Test
    void shouldReturnOnlyDestinationsWithRatingAbove48() {
        List<Destination> destinations = List.of(
            new Destination("Paris", "France", 4.8),
            new Destination("Tokyo", "Japan", 4.9),
            new Destination("Reykjavik", "Iceland", 4.7)
        );

        List<Destination> recommendations = recommendationService.getRecommendations(destinations);

        assertEquals(1, recommendations.size());
        assertEquals("Tokyo", recommendations.get(0).getName());
        assertTrue(recommendations.get(0).getRating() > 4.8);
    }
}
