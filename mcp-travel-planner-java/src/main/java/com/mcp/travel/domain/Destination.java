package com.mcp.travel.domain;

public class Destination {
    private final String name;
    private final String country;
    private final double rating;

    public Destination(String name, String country, double rating) {
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
