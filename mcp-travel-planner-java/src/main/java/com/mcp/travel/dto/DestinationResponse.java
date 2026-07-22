package com.mcp.travel.dto;

public class DestinationResponse {
    private final String name;
    private final String country;
    private final double rating;

    public DestinationResponse(String name, String country, double rating) {
        this.name = name;
        this.country = country;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getRating() {
        return rating;
    }
}
