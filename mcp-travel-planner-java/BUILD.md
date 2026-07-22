# How to Build and Run

## Build
```bash
mvn clean package
```

## Run
```bash
java -jar target/mcp-travel-planner-java-1.0.0.jar
```

or

```bash
mvn spring-boot:run
```

## Test API
```bash
curl -X POST http://localhost:8080/api/itinerary \
  -H "Content-Type: application/json" \
  -d '{
    "destination": "Paris",
    "numDays": 3,
    "startDate": "2024-07-01",
    "budget": 1500,
    "preferences": "Museums and cafes",
    "openaiApiKey": "sk-...",
    "googleMapsApiKey": "AIza..."
  }'
```
