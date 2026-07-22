# API Testing Guide

## Fixed Issues

### 1. ✅ Created ItineraryRequest DTO
New class with fields:
- `destination` (String) - Required
- `budget` (int) - Min: 100, Max: 100,000
- `days` (int) - Min: 1, Max: 30

### 2. ✅ Updated Controller
- Added new POST endpoint: `/api/itinerary`
- Existing full endpoint moved to: `/api/itinerary/full`
- Fixed all imports and annotations
- Added configuration-based API keys

### 3. ✅ Fixed Compilation Issues
- All 8 Java files compile successfully
- No missing imports
- All annotations correct

## API Endpoints

### 1. Simple Itinerary Generation (NEW)

**Endpoint:** `POST /api/itinerary`

**Request Body:**
```json
{
  "destination": "Paris",
  "budget": 2000,
  "days": 7
}
```

**Response:**
```json
{
  "itinerary": "# Trip to Paris\n\n...",
  "usedAirbnbData": true,
  "message": "Used real Airbnb listings..."
}
```

**Response Headers:**
- `X-Session-Id` - Use for downloading calendar

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/itinerary \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "Paris",
    "budget": 2000,
    "days": 7
  }'
```

---

### 2. Full Itinerary Generation (EXISTING)

**Endpoint:** `POST /api/itinerary/full`

**Request Body:**
```json
{
  "destination": "Tokyo",
  "numDays": 5,
  "startDate": "2024-07-01",
  "budget": 3000,
  "preferences": "Anime, technology",
  "quickPreferences": ["Adventure", "Food"],
  "openaiApiKey": "sk-...",
  "googleMapsApiKey": "AIza..."
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/itinerary/full \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "Tokyo",
    "numDays": 5,
    "startDate": "2024-07-01",
    "budget": 3000,
    "preferences": "Anime, technology",
    "openaiApiKey": "sk-your-key",
    "googleMapsApiKey": "AIza-your-key"
  }'
```

---

### 3. Download Calendar

**Endpoint:** `GET /api/itinerary/download?sessionId={sessionId}`

**Response:** `.ics` file

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/itinerary/download?sessionId=abc123" \
  -o travel_itinerary.ics
```

---

### 4. Health Check

**Endpoint:** `GET /api/health`

**Response:**
```json
{
  "status": "UP",
  "service": "MCP Travel Planner"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/health
```

## Configuration

### Environment Variables

Set these in your environment or in `application.properties`:

```bash
export OPENAI_API_KEY=sk-your-openai-key
export GOOGLE_MAPS_API_KEY=AIza-your-google-maps-key
```

### application.properties

```properties
openai.api.key=${OPENAI_API_KEY:}
google.maps.api.key=${GOOGLE_MAPS_API_KEY:}
```

## Running the Application

```bash
cd mcp-travel-planner-java
mvn clean install
mvn spring-boot:run
```

Expected output:
```
✅ MCP Travel Planner Agents running in Java!
🌍 API available at: http://localhost:8080
📋 Health check: http://localhost:8080/api/health
🚀 Generate itinerary: POST http://localhost:8080/api/itinerary
Started TravelPlannerApp in X.XXX seconds
```

## Validation Rules

### ItineraryRequest
- `destination`: Required, cannot be blank
- `budget`: Min 100, Max 100,000
- `days`: Min 1, Max 30

### TravelRequest
- `destination`: Required, cannot be blank
- `numDays`: Min 1, Max 30
- `startDate`: Required, valid date
- `budget`: Min 100, Max 100,000
- `openaiApiKey`: Required
- `googleMapsApiKey`: Required

## Error Responses

**400 Bad Request** - Validation error:
```json
{
  "timestamp": "2024-06-10T12:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/itinerary"
}
```

**500 Internal Server Error** - Processing error:
```json
{
  "itinerary": null,
  "usedAirbnbData": false,
  "message": "Error: Failed to generate itinerary..."
}
```

## Testing with Postman

1. Import the endpoints into Postman
2. Set method to POST
3. Add Content-Type header: `application/json`
4. Add request body (see examples above)
5. Send request
6. Check response and X-Session-Id header
7. Use session ID to download calendar

## Project Structure

```
src/main/java/com/mcp/travel/
├── TravelPlannerApp.java          # Main Spring Boot app
├── controller/
│   └── TravelController.java      # REST endpoints
├── dto/
│   ├── ItineraryRequest.java      # Simple request DTO (NEW)
│   ├── TravelRequest.java         # Full request DTO
│   └── TravelResponse.java        # Response DTO
├── service/
│   ├── McpService.java            # MCP & OpenAI integration
│   └── IcsService.java            # Calendar generation
└── config/
    └── WebConfig.java             # CORS configuration
```

## Build Information

- **Java Version:** 11
- **Spring Boot:** 2.7.18
- **Build Tool:** Maven
- **Compiled Files:** 8 Java source files
- **Build Status:** ✅ SUCCESS
- **JAR:** mcp-travel-planner-java-1.0.0.jar
