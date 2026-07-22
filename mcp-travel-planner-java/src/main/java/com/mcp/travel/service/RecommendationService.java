package com.mcp.travel.service;

import com.mcp.travel.domain.Destination;

import java.util.List;

public interface RecommendationService {
    List<Destination> getRecommendations(List<Destination> destinations);
}
