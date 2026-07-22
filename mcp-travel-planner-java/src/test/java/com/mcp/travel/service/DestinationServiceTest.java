package com.mcp.travel.service;

import com.mcp.travel.domain.Destination;
import com.mcp.travel.service.impl.DefaultDestinationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DestinationServiceTest {

    private final DestinationService destinationService = new DefaultDestinationService();

    @Test
    void shouldReturnAListOfDestinationsWithExpectedValues() {
        List<Destination> destinations = destinationService.getDestinations();

        assertNotNull(destinations);
        assertFalse(destinations.isEmpty());
        assertEquals(3, destinations.size());

        Destination first = destinations.get(0);
        assertEquals("Paris", first.getName());
        assertEquals("France", first.getCountry());
        assertEquals(4.8, first.getRating());
    }
}
