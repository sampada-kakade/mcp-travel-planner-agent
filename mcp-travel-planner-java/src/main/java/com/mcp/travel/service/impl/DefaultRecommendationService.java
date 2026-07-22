package com.mcp.travel.service.impl;

import com.mcp.travel.domain.Destination;
import com.mcp.travel.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultRecommendationService implements RecommendationService {

    @Override
    public List<Destination> getRecommendations(List<Destination> destinations) {
        return destinations.stream()
            .filter(destination -> destination.getRating() > 4.8)
            .collect(Collectors.toList());
    }
}
