# Quick Start Guide

## ⚡ One-Line Start

```bash
mvn spring-boot:run
```

## 🎯 Test API

```bash
curl -X POST http://localhost:8080/api/itinerary \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "Paris",
    "numDays": 3,
    "startDate": "2024-07-01",
    "budget": 1500,
    "preferences": "Art and food",
    "openaiApiKey": "YOUR_OPENAI_KEY",
    "googleMapsApiKey": "YOUR_GOOGLE_MAPS_KEY"
  }'
```

## 📋 File Structure

```
src/main/java/com/mcp/travel/
├── TravelPlannerApp.java       ← Main app
├── controller/
│   └── TravelController.java   ← REST endpoints
├── service/
│   ├── McpService.java         ← MCP + OpenAI
│   └── IcsService.java         ← Calendar
├── dto/
│   ├── TravelRequest.java      ← Input
│   └── TravelResponse.java     ← Output
└── config/
    └── WebConfig.java          ← CORS
```

## ✅ All Files Valid

All Java files are correctly placed in `src/main/java/com/mcp/travel/`
All resources are in `src/main/resources/`

**No garbage files detected!** ✨
