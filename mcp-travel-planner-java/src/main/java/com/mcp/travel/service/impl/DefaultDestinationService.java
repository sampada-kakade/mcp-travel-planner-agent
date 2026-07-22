package com.mcp.travel.service.impl;

import com.mcp.travel.domain.Destination;
import com.mcp.travel.service.DestinationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultDestinationService implements DestinationService {

    @Override
    public List<Destination> getDestinations() {
        return List.of(
            new Destination("Paris", "France", 4.8),
            new Destination("Tokyo", "Japan", 4.9),
            new Destination("Reykjavik", "Iceland", 4.7)
        );
    }
}
