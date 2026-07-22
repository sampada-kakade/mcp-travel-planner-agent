# 🌍 MCP Travel Planner - Java Edition

A Spring Boot REST API implementation of the AI travel planning application with MCP servers integration.

## ✨ Features

- **REST API**: Spring Boot endpoints for travel planning
- **MCP Integration**: Connects to Airbnb and Google Maps MCP servers
- **OpenAI GPT-4o**: Intelligent itinerary generation
- **Calendar Export**: Download itineraries as .ics files
- **Real-time Data**: Airbnb listings and Google Maps integration

## 📋 API Endpoints

### Generate Itinerary
```
POST /api/itinerary
Content-Type: application/json

{
  "destination": "Paris",
  "numDays": 7,
  "startDate": "2024-06-01",
  "budget": 2000,
  "preferences": "Cultural sites, food",
  "quickPreferences": ["Adventure", "Sightseeing"],
  "openaiApiKey": "sk-...",
  "googleMapsApiKey": "AIza..."
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

### Download Calendar
```
GET /api/itinerary/download?sessionId={sessionId}
```

Returns `.ics` file for calendar import.

### Health Check
```
GET /api/health
```

### Destinations
```
GET /api/destinations
```

Response example:
```json
[
  {
    "name": "Paris",
    "country": "France",
    "rating": 4.8
  }
]
```

### Recommendations
```
GET /api/recommendations
```

Returns destinations with rating above 4.8.

Response example:
```json
[
  {
    "name": "Tokyo",
    "country": "Japan",
    "rating": 4.9
  }
]
```

## 🚀 Local Run

### Run from the project folder
```bash
cd mcp-travel-planner-java
SERVER_PORT=8083 java -jar ./target/mcp-travel-planner-java-1.0.0.jar
```

### Test the health endpoint
```bash
curl http://localhost:8083/api/health
```

Expected response:
```json
{
  "status": "UP",
  "service": "MCP Travel Planner"
}
```

### Test the destinations and recommendations endpoints
```bash
curl http://localhost:8083/api/destinations
curl http://localhost:8083/api/recommendations
```

### Open the frontend
1. Start the app on port 8083.
2. Open your browser at http://localhost:8083/.
3. Click the refresh button to load the latest recommendations from /api/recommendations.

### Run with VS Code
- Open the workspace root in VS Code.
- Use the debug configuration from .vscode/launch.json.
- Start the app with Run and Debug.

## 🚀 Setup

### Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **Node.js** (for MCP servers)
- **OpenAI API Key**: https://platform.openai.com/api-keys
- **Google Maps API Key**: https://console.cloud.google.com/apis/credentials

### Installation

1. **Clone and navigate:**
   ```bash
   cd mcp-travel-planner-java
   ```

2. **Set environment variables:**
   ```bash
   export OPENAI_API_KEY=sk-...
   export GOOGLE_MAPS_API_KEY=AIza...
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR:
   ```bash
   java -jar target/mcp-travel-planner-java-1.0.0.jar
   ```

5. **Access the API:**
   - API Base URL: `http://localhost:8080`
   - Health Check: `http://localhost:8080/api/health`

## 🏗️ Project Structure

```
mcp-travel-planner-java/
├── src/main/java/com/mcp/travel/
│   ├── TravelPlannerApp.java          # Main Spring Boot application
│   ├── controller/
│   │   └── TravelController.java      # REST endpoints
│   ├── service/
│   │   ├── McpService.java            # MCP orchestration & OpenAI calls
│   │   └── IcsService.java            # Calendar file generation
│   ├── dto/
│   │   ├── TravelRequest.java         # Request DTO
│   │   └── TravelResponse.java        # Response DTO
│   └── config/
│       └── WebConfig.java             # CORS configuration
├── src/main/resources/
│   └── application.properties         # Configuration
└── pom.xml                            # Maven dependencies
```

## 🧪 Testing with cURL

```bash
curl -X POST http://localhost:8080/api/itinerary \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "Tokyo",
    "numDays": 5,
    "startDate": "2024-07-01",
    "budget": 3000,
    "preferences": "Anime culture, technology, food",
    "openaiApiKey": "sk-...",
    "googleMapsApiKey": "AIza..."
  }'
```

## 🔧 Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8080
openai.model=gpt-4o
mcp.timeout.seconds=60
```

## 📦 Dependencies

- **Spring Boot 3.2.5** - Web framework
- **OkHttp 4.12.0** - HTTP client
- **iCal4j 3.2.14** - Calendar file generation
- **Gson** - JSON processing
- **Lombok** - Boilerplate reduction

## 🎯 Key Differences from Python Version

| Feature | Python | Java |
|---------|--------|------|
| Framework | Streamlit | Spring Boot REST API |
| UI | Built-in web UI | Headless API (use Postman/cURL) |
| MCP Library | `agno` framework | Direct subprocess calls |
| Async | `asyncio` | Java threads with OkHttp |
| Calendar | `icalendar` | `iCal4j` |

## 🛠️ Development

**Compile:**
```bash
mvn clean compile
```

**Run tests:**
```bash
mvn test
```

**Package:**
```bash
mvn package
```

## 📝 Notes

- The Java version exposes a REST API instead of a web UI
- Use Postman, Insomnia, or build a frontend to consume the API
- MCP processes are spawned as subprocesses (requires Node.js and npx)
- Session-based caching for itinerary downloads

## 🔮 Future Enhancements

- [ ] Add JavaFX UI
- [ ] WebSocket support for real-time updates
- [ ] Database persistence
- [ ] Docker containerization
- [ ] Kubernetes deployment configs
