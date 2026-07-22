package com.mcp.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravelPlannerApp {
    
    public static void main(String[] args) {
        SpringApplication.run(TravelPlannerApp.class, args);
        String port = System.getProperty("server.port", System.getenv().getOrDefault("SERVER_PORT", "8082"));
        System.out.println("✅ MCP Travel Planner Agents running in Java!");
        System.out.println("🌍 API available at: http://localhost:" + port);
        System.out.println("📋 Health check: http://localhost:" + port + "/api/health");
        System.out.println("🚀 Generate itinerary: POST http://localhost:" + port + "/api/itinerary");
    }
}
