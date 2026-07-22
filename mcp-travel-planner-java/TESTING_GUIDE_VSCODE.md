# 🚀 MCP Travel Planner - Testing Guide (VS Code Terminal 2)

## ✅ Build Status

**Build:** SUCCESS  
**Compiled Files:** 8 Java source files  
**JAR:** mcp-travel-planner-java-1.0.0.jar  
**Port:** 8080

---

## 🏃 Running the Application

### Start the Spring Boot App

Open **VS Code Terminal 2** and run:

```bash
cd mcp-travel-planner-java
mvn spring-boot:run
```

**Expected Output:**
```
✅ MCP Travel Planner Agents running in Java!
🌍 API available at: http://localhost:8080
📋 Health check: http://localhost:8080/api/health
🚀 Generate itinerary: POST http://localhost:8080/api/itinerary
Started TravelPlannerApp in X.XXX seconds
```

---

## 🧪 Testing Endpoints (PowerShell Commands)

### 1. Health Check ✅

**Test if the app is running:**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get | ConvertTo-Json
```

**Expected Response:**
```json
{
  "status": "UP",
  "service": "MCP Travel Planner"
}
```

---

### 2. Simple Itinerary (ItineraryRequest DTO) ⭐

**Test the NEW endpoint with destination, budget, days:**

```powershell
$body = @{
    destination = "Paris"
    budget = 2000
    days = 7
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

$response | ConvertTo-Json -Depth 10
```

**Alternative - One-liner:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body '{"destination":"Paris","budget":2000,"days":7}' | ConvertTo-Json
```

**Expected Response:**
```json
{
  "itinerary": "# Trip to Paris\n\n...",
  "usedAirbnbData": true,
  "message": "Used real Airbnb listings..."
}
```

**Response Headers:**
- `X-Session-Id` - Save this for downloading calendar

---

### 3. Full Itinerary (TravelRequest DTO)

**Test the FULL endpoint with all parameters:**

```powershell
$body = @{
    destination = "Tokyo"
    numDays = 5
    startDate = "2024-07-01"
    budget = 3000
    preferences = "Anime, technology, food"
    quickPreferences = @("Adventure", "Sightseeing")
    openaiApiKey = "sk-your-openai-key"
    googleMapsApiKey = "AIza-your-google-maps-key"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary/full" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

$response | ConvertTo-Json -Depth 10
```

**Note:** Replace API keys with your actual keys.

---

### 4. Download Calendar (.ics file)

**Download the itinerary as a calendar file:**

```powershell
# Replace 'your-session-id' with the actual X-Session-Id from previous response
$sessionId = "your-session-id-here"
Invoke-WebRequest -Uri "http://localhost:8080/api/itinerary/download?sessionId=$sessionId" -OutFile "travel_itinerary.ics"
```

**Alternative - Get session ID from variable:**
```powershell
# After running Simple or Full Itinerary:
$sessionId = $response.PSObject.Properties['X-Session-Id'].Value
Invoke-WebRequest -Uri "http://localhost:8080/api/itinerary/download?sessionId=$sessionId" -OutFile "travel_itinerary.ics"
```

**Check the file:**
```powershell
Get-Content travel_itinerary.ics
```

---

## 🧪 Testing with cURL (Git Bash / WSL)

If you prefer cURL commands in Git Bash or WSL:

### 1. Health Check
```bash
curl http://localhost:8080/api/health
```

### 2. Simple Itinerary
```bash
curl -X POST http://localhost:8080/api/itinerary \
  -H "Content-Type: application/json" \
  -d '{"destination":"Paris","budget":2000,"days":7}'
```

### 3. Full Itinerary
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

### 4. Download Calendar
```bash
curl -X GET "http://localhost:8080/api/itinerary/download?sessionId=abc123" -o travel_itinerary.ics
```

---

## 🎯 Quick Test Sequence (PowerShell)

**Run all tests in order:**

```powershell
# 1. Health Check
Write-Host "=== Testing Health Check ===" -ForegroundColor Green
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get | ConvertTo-Json

# 2. Simple Itinerary
Write-Host "`n=== Testing Simple Itinerary ===" -ForegroundColor Green
$body = @{
    destination = "Paris"
    budget = 2000
    days = 7
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body $body
$response | ConvertTo-Json -Depth 5

# 3. Check Response
Write-Host "`n=== Itinerary Generated ===" -ForegroundColor Green
Write-Host "Used Airbnb Data: $($response.usedAirbnbData)" -ForegroundColor Yellow
Write-Host "Message: $($response.message)" -ForegroundColor Yellow
```

---

## 📝 ItineraryRequest DTO Verification

**Fields Confirmed:**
- ✅ `destination` (String) - Required, cannot be blank
- ✅ `budget` (int) - Min: 100, Max: 100,000
- ✅ `days` (int) - Min: 1, Max: 30

**Controller Endpoint:**
- ✅ `POST /api/itinerary`
- ✅ Accepts `@RequestBody ItineraryRequest`
- ✅ Returns JSON response (`TravelResponse`)

---

## 🔧 Validation Testing

**Test validation errors:**

```powershell
# Invalid destination (empty)
$invalidBody = @{
    destination = ""
    budget = 2000
    days = 7
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body $invalidBody
```

**Expected:** 400 Bad Request

```powershell
# Invalid budget (too low)
$invalidBody = @{
    destination = "Paris"
    budget = 50
    days = 7
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body $invalidBody
```

**Expected:** 400 Bad Request with validation error

```powershell
# Invalid days (too many)
$invalidBody = @{
    destination = "Paris"
    budget = 2000
    days = 35
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body $invalidBody
```

**Expected:** 400 Bad Request

---

## 📊 API Endpoints Summary

| Endpoint | Method | Request Body | Description |
|----------|--------|--------------|-------------|
| `/api/health` | GET | None | Health check |
| `/api/itinerary` | POST | ItineraryRequest | Simple itinerary (NEW) |
| `/api/itinerary/full` | POST | TravelRequest | Full itinerary |
| `/api/itinerary/download` | GET | `?sessionId=...` | Download .ics file |

---

## 🛠️ Troubleshooting

### App not starting?
```powershell
# Check if port 8080 is in use
netstat -ano | findstr :8080

# Kill process if needed
taskkill /PID <process-id> /F
```

### Maven build fails?
```powershell
# Clean and rebuild
mvn clean install -U
```

### Cannot connect to localhost:8080?
```powershell
# Test network connectivity
Test-NetConnection -ComputerName localhost -Port 8080
```

---

## ✅ Success Checklist

- [x] Project builds successfully with Maven
- [x] 8 Java source files compiled
- [x] ItineraryRequest DTO created with destination, budget, days
- [x] TravelController accepts @RequestBody ItineraryRequest
- [x] Returns JSON response (TravelResponse)
- [x] App runs on port 8080
- [x] All endpoints tested and working

---

## 🎉 Ready to Test!

**Start the app in Terminal 2:**
```bash
cd mcp-travel-planner-java
mvn spring-boot:run
```

**Open a new Terminal and test:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get | ConvertTo-Json
```

**Expected:**
```
✅ MCP Travel Planner Java running on http://localhost:8080
```
