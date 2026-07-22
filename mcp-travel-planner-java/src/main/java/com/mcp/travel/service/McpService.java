package com.mcp.travel.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class McpService {
    
    private static final Logger logger = LoggerFactory.getLogger(McpService.class);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    
    @Value("${mcp.airbnb.command}")
    private String airbnbCommand;
    
    @Value("${mcp.travelplanner.command}")
    private String travelPlannerCommand;
    
    @Value("${mcp.timeout.seconds}")
    private int timeoutSeconds;
    
    @Value("${openai.model}")
    private String openaiModel;
    
    @Value("${openai.api.key:}")
    private String configuredApiKey;
    
    private final OkHttpClient httpClient;
    private final Gson gson;
    
    public McpService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }
    
    public String runTravelPlanner(String destination, int numDays, String preferences, 
                                   int budget, String openaiApiKey, String googleMapsApiKey) {
        
        logger.info("Starting travel planner for destination: {}", destination);
        
        // Validate API key
        String effectiveApiKey = (openaiApiKey != null && !openaiApiKey.isEmpty()) ? 
            openaiApiKey : configuredApiKey;
        
        if (effectiveApiKey == null || effectiveApiKey.isEmpty()) {
            logger.error("OpenAI API key is not configured");
            throw new IllegalArgumentException("OpenAI API key is required. Please set OPENAI_API_KEY environment variable or provide it in the request.");
        }
        
        if (effectiveApiKey.length() < 20) {
            logger.error("OpenAI API key appears to be invalid (too short)");
            throw new IllegalArgumentException("OpenAI API key appears to be invalid. Please check your API key.");
        }
        
        logger.debug("Using OpenAI API key: {}...", effectiveApiKey.substring(0, Math.min(10, effectiveApiKey.length())));
        
        List<Process> mcpProcesses = new ArrayList<>();
        
        try {
            // Set environment variable
            Map<String, String> env = new HashMap<>(System.getenv());
            env.put("GOOGLE_MAPS_API_KEY", googleMapsApiKey);
            
            // Start MCP processes (simulate connection)
            logger.info("Connecting to MCP servers...");
            Process airbnbProcess = startMcpProcess(airbnbCommand, env);
            Process travelPlannerProcess = startMcpProcess(travelPlannerCommand, env);
            
            if (airbnbProcess != null) mcpProcesses.add(airbnbProcess);
            if (travelPlannerProcess != null) mcpProcesses.add(travelPlannerProcess);
            
            // Build the prompt
            String prompt = buildPrompt(destination, numDays, preferences, budget);
            
            // Call OpenAI API with validated key
            String itinerary = callOpenAI(prompt, effectiveApiKey);
            
            logger.info("Successfully generated itinerary");
            return itinerary;
            
        } catch (Exception e) {
            logger.error("Error running travel planner", e);
            throw new RuntimeException("Failed to generate itinerary: " + e.getMessage(), e);
        } finally {
            // Clean up MCP processes
            for (Process process : mcpProcesses) {
                if (process != null && process.isAlive()) {
                    process.destroy();
                }
            }
        }
    }
    
    private Process startMcpProcess(String command, Map<String, String> env) {
        try {
            logger.debug("Starting MCP process: {}", command);
            
            ProcessBuilder pb = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                pb.command("cmd.exe", "/c", command);
            } else {
                pb.command("sh", "-c", command);
            }
            
            pb.environment().putAll(env);
            Process process = pb.start();
            
            // Give it a moment to start
            Thread.sleep(2000);
            
            return process;
        } catch (Exception e) {
            logger.warn("Failed to start MCP process: {}", command, e);
            return null;
        }
    }
    
    private String buildPrompt(String destination, int numDays, String preferences, int budget) {
        return "IMMEDIATELY create an extremely detailed and comprehensive travel itinerary for:\n\n" +
            "**Destination:** " + destination + "\n" +
            "**Duration:** " + numDays + " days\n" +
            "**Budget:** $" + budget + " USD total\n" +
            "**Preferences:** " + preferences + "\n\n" +
            "DO NOT ask any questions. Generate a complete, highly detailed itinerary now using all available tools.\n\n" +
            "**CRITICAL REQUIREMENTS:**\n" +
            "- Use Google Maps MCP to calculate distances and travel times between ALL locations\n" +
            "- Include specific addresses for every location, restaurant, and attraction\n" +
            "- Provide detailed timing for each activity with buffer time between locations\n" +
            "- Calculate precise costs for transportation between each location\n" +
            "- Include opening hours, ticket prices, and best visiting times for all attractions\n" +
            "- Provide detailed weather information and specific packing recommendations\n\n" +
            "**REQUIRED OUTPUT FORMAT:**\n" +
            "1. **Trip Overview** - Summary, total estimated cost breakdown, detailed weather forecast\n" +
            "2. **Accommodation** - 3 specific Airbnb options with real prices, addresses, amenities, and distance from city center\n" +
            "3. **Transportation Overview** - Detailed transportation options, costs, and recommendations\n" +
            "4. **Day-by-Day Itinerary** - Extremely detailed schedule with:\n" +
            "   - Specific start/end times for each activity\n" +
            "   - Exact distances and travel times between locations (use Google Maps MCP)\n" +
            "   - Detailed descriptions of each location with addresses\n" +
            "   - Opening hours, ticket prices, and best visiting times\n" +
            "   - Estimated costs for each activity and transportation\n" +
            "   - Buffer time between activities for unexpected delays\n" +
            "5. **Dining Plan** - Specific restaurants with addresses, price ranges, cuisine types, and distance from accommodation\n" +
            "6. **Detailed Practical Information**:\n" +
            "   - Weather forecast with clothing recommendations\n" +
            "   - Currency exchange rates and costs\n" +
            "   - Local transportation options and costs\n" +
            "   - Safety information and emergency contacts\n" +
            "   - Cultural norms and etiquette tips\n" +
            "   - Communication options (SIM cards, WiFi, etc.)\n" +
            "   - Health and medical considerations\n" +
            "   - Shopping and souvenir recommendations\n\n" +
            "Use Airbnb MCP for real accommodation data, Google Maps MCP for ALL distance calculations and location services, and web search for current information.\n" +
            "Make reasonable assumptions and fill in any gaps with your knowledge.\n" +
            "Generate the complete, highly detailed itinerary in one response without asking for clarification.";
    }
    
    private String callOpenAI(String prompt, String apiKey) throws IOException {
        logger.debug("Calling OpenAI API with model: {}", openaiModel);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", openaiModel);
        
        JsonArray messages = new JsonArray();
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", 
            "You are a professional travel consultant AI that creates highly detailed travel itineraries directly without asking questions.\n\n" +
            "You have access to:\n" +
            "🏨 Airbnb listings with real availability and current pricing\n" +
            "🗺️ Google Maps MCP for location services, directions, distance calculations, and local navigation\n" +
            "🔍 Web search capabilities for current information, reviews, and travel updates\n\n" +
            "ALWAYS create a complete, detailed itinerary immediately without asking for clarification or additional information.\n" +
            "Use Google Maps MCP extensively to calculate distances between all locations and provide precise travel times.\n" +
            "If information is missing, use your best judgment and available tools to fill in the gaps.");
        
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        
        messages.add(systemMessage);
        messages.add(userMessage);
        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.7);
        requestBody.addProperty("max_tokens", 4000);
        
        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse("application/json")
        );
        
        Request request = new Request.Builder()
            .url(OPENAI_API_URL)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details";
                logger.error("OpenAI API call failed. Status: {}, Message: {}, Body: {}", 
                    response.code(), response.message(), errorBody);
                
                if (response.code() == 401) {
                    throw new IOException("OpenAI API authentication failed (401 Unauthorized). Please verify your API key is correct and has not expired. Error: " + errorBody);
                } else if (response.code() == 429) {
                    throw new IOException("OpenAI API rate limit exceeded (429). Please check your usage limits or try again later.");
                } else if (response.code() >= 500) {
                    throw new IOException("OpenAI API server error (" + response.code() + "). Please try again later.");
                } else {
                    throw new IOException("OpenAI API call failed: " + response.code() + " - " + response.message() + ". Details: " + errorBody);
                }
            }
            
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            return jsonResponse.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
        }
    }
}
