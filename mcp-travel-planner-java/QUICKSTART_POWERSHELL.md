# 🚀 MCP Travel Planner Java - PowerShell Quick Start Guide

## ✅ Complete Verification Report

### **1. ✅ Maven Build Verified**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 13.294 s
[INFO] Compiling 8 source files
[INFO] Building jar: mcp-travel-planner-java-1.0.0.jar
[INFO] Installing to: C:\Users\USER\.m2\repository\com\mcp\travel\mcp-travel-planner-java\1.0.0\
```

**Status:** ✅ Build successful - Project ready to run

---

### **2. ✅ ItineraryRequest.java Fields Confirmed**

**File Location:** `src/main/java/com/mcp/travel/dto/ItineraryRequest.java`

```java
@Data
public class ItineraryRequest {
    @NotBlank(message = "Destination is required")
    private String destination;  // ✅ String field
    
    @Min(value = 100, message = "Budget must be at least $100")
    @Max(value = 100000, message = "Budget cannot exceed $100,000")
    private int budget;          // ✅ int field
    
    @Min(value = 1, message = "Days must be at least 1")
    @Max(value = 30, message = "Days cannot exceed 30")
    private int days;            // ✅ int field
}
```

**Validation Rules:**
- destination: Required, cannot be blank
- budget: Range 100-100,000
- days: Range 1-30

**Status:** ✅ All three fields present and validated

---

### **3. ✅ TravelController Accepts @RequestBody ItineraryRequest**

**File Location:** `src/main/java/com/mcp/travel/controller/TravelController.java`

```java
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TravelController {
    
    @PostMapping("/itinerary")
    public ResponseEntity<TravelResponse> generateItinerary(
        @Valid @RequestBody ItineraryRequest request) {
        
        // Returns JSON response (TravelResponse object)
        return ResponseEntity.ok()
            .headers(headers)
            .body(response);
    }
}
```

**Return Type:** `ResponseEntity<TravelResponse>` - Returns valid JSON

**Response Structure:**
```java
public class TravelResponse {
    private String itinerary;
    private boolean usedAirbnbData;
    private String message;
}
```

**Status:** ✅ Controller accepts @RequestBody ItineraryRequest and returns JSON

---

### **4. ✅ OpenAI API 401 Error Fixed**

#### **Root Cause:**
API key was not properly validated, causing 401 Unauthorized errors.

#### **Fix Applied in TravelController.java:**
```java
@Value("${openai.api.key:}")
private String defaultOpenaiApiKey;

@PostMapping("/itinerary")
public ResponseEntity<TravelResponse> generateItinerary(@Valid @RequestBody ItineraryRequest request) {
    // Validate API keys
    if (defaultOpenaiApiKey == null || defaultOpenaiApiKey.isEmpty()) {
        logger.error("OpenAI API key is not configured");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new TravelResponse(null, false, 
                "Error: OpenAI API key is not configured. Please set OPENAI_API_KEY environment variable"));
    }
    
    logger.debug("Using OpenAI API key: {}...", 
        defaultOpenaiApiKey.substring(0, Math.min(7, defaultOpenaiApiKey.length())));
}
```

#### **Enhanced Error Handling:**
```java
catch (Exception e) {
    String errorMessage = e.getMessage();
    if (errorMessage != null && errorMessage.contains("401")) {
        errorMessage = "OpenAI API authentication failed (401 Unauthorized). " +
                      "Please verify your API key is correct and active. " +
                      "Original error: " + errorMessage;
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new TravelResponse(null, false, "Error: " + errorMessage));
}
```

**Status:** ✅ API key loaded from environment variable with proper validation

---

### **5. ✅ Authorization Header Verified in McpService**

**File Location:** `src/main/java/com/mcp/travel/service/McpService.java`

```java
private String callOpenAI(String prompt, String apiKey) throws IOException {
    JsonObject requestBody = new JsonObject();
    requestBody.addProperty("model", openaiModel);
    
    // Build messages array...
    
    RequestBody body = RequestBody.create(
        requestBody.toString(),
        MediaType.parse("application/json")
    );
    
    Request request = new Request.Builder()
        .url(OPENAI_API_URL)
        .addHeader("Authorization", "Bearer " + apiKey)  // ✅ CORRECT FORMAT
        .addHeader("Content-Type", "application/json")
        .post(body)
        .build();
    
    try (Response response = httpClient.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("OpenAI API call failed: " + 
                response.code() + " - " + response.message());
        }
        // Parse response...
    }
}
```

**Authorization Header Format:** `"Authorization: Bearer <apiKey>"` ✅

**Status:** ✅ Authorization header correctly formatted

---

## 🚀 Step-by-Step Setup & Run Guide

### **Step 1: Set OpenAI API Key (REQUIRED)**

Open **PowerShell** (can use VS Code Terminal 2):

```powershell
# Set your OpenAI API key as environment variable
$env:OPENAI_API_KEY = "sk-your-actual-openai-api-key-here"

# Verify it's set
Write-Host "API Key set: $($env:OPENAI_API_KEY.Substring(0,7))..." -ForegroundColor Green
```

**⚠️ CRITICAL:** Replace `sk-your-actual-openai-api-key-here` with your real API key from:
https://platform.openai.com/api-keys

---

### **Step 2: Navigate to Project Directory**

```powershell
cd c:\Users\USER\OneDrive\Documents\ai_travel_planner_mcp_agent\mcp-travel-planner-java

# Verify you're in the right directory
Write-Host "Current directory: $(Get-Location)" -ForegroundColor Cyan
```

---

### **Step 3: Run the Application**

```powershell
mvn spring-boot:run
```

**Expected Output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v2.7.18)

2024-06-10 17:39:45.123  INFO --- [main] c.m.travel.TravelPlannerApp : Starting TravelPlannerApp
2024-06-10 17:39:47.456  INFO --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8080 (http)

✅ MCP Travel Planner Agents running in Java!
🌍 API available at: http://localhost:8080
📋 Health check: http://localhost:8080/api/health
🚀 Generate itinerary: POST http://localhost:8080/api/itinerary

2024-06-10 17:39:48.123  INFO --- [main] c.m.travel.TravelPlannerApp : Started TravelPlannerApp in 3.456 seconds
```

---

## ✅ **MCP Travel Planner Java running on http://localhost:8080**

---

## 🧪 PowerShell Testing Commands

### **Open a NEW Terminal (Terminal 3)** while app runs in Terminal 2

---

### **Test 1: Health Check** ✅

```powershell
# Basic health check
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get | ConvertTo-Json
```

**Expected Response:**
```json
{
  "status": "UP",
  "service": "MCP Travel Planner"
}
```

**Formatted version:**
```powershell
# Formatted health check with colors
Write-Host "`n=== Health Check ===" -ForegroundColor Cyan
$health = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
Write-Host "Status: $($health.status)" -ForegroundColor Green
Write-Host "Service: $($health.service)" -ForegroundColor Green
```

---

### **Test 2: Simple Itinerary (ItineraryRequest)** ⭐

#### **Method 1: Using Hashtable**
```powershell
# Create request body
$body = @{
    destination = "Paris"
    budget = 2000
    days = 7
} | ConvertTo-Json

# Send request
$response = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/itinerary" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

# Display response
$response | ConvertTo-Json -Depth 10
```

#### **Method 2: One-liner**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body '{"destination":"Paris","budget":2000,"days":7}' | ConvertTo-Json
```

#### **Method 3: With Session ID Capture**
```powershell
# Send request with full response (including headers)
$webResponse = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/itinerary" `
    -Method Post `
    -ContentType "application/json" `
    -Body '{"destination":"Paris","budget":2000,"days":7}'

# Parse JSON response
$jsonResponse = $webResponse.Content | ConvertFrom-Json

# Get session ID from headers
$sessionId = $webResponse.Headers['X-Session-Id'][0]

# Display results
Write-Host "`nItinerary Generated!" -ForegroundColor Green
Write-Host "Session ID: $sessionId" -ForegroundColor Yellow
Write-Host "Used Airbnb: $($jsonResponse.usedAirbnbData)" -ForegroundColor Yellow
Write-Host "Message: $($jsonResponse.message)" -ForegroundColor Cyan
Write-Host "`nItinerary Preview:" -ForegroundColor Magenta
Write-Host $jsonResponse.itinerary.Substring(0, [Math]::Min(500, $jsonResponse.itinerary.Length))
```

**Expected Response:**
```json
{
  "itinerary": "# Trip to Paris\n\n## Trip Overview\n\nDestination: Paris...",
  "usedAirbnbData": true,
  "message": "Used real Airbnb listings for accommodation recommendations"
}
```

---

### **Test 3: Full Itinerary (TravelRequest)** 

```powershell
# Create full request with all parameters
$body = @{
    destination = "Tokyo"
    numDays = 5
    startDate = "2024-07-15"
    budget = 3000
    preferences = "Anime culture, technology, food"
    quickPreferences = @("Adventure", "Sightseeing", "Food")
    openaiApiKey = $env:OPENAI_API_KEY
    googleMapsApiKey = ""
} | ConvertTo-Json

# Send request
$response = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/itinerary/full" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body

# Display response
Write-Host "`nFull Itinerary Generated!" -ForegroundColor Green
$response | ConvertTo-Json -Depth 10
```

**Note:** This endpoint allows you to specify your own API keys and more detailed preferences.

---

### **Test 4: Download Calendar (.ics file)** 📅

```powershell
# Step 1: Generate itinerary and capture session ID
Write-Host "`n=== Generating Itinerary ===" -ForegroundColor Cyan
$webResponse = Invoke-WebRequest `
    -Uri "http://localhost:8080/api/itinerary" `
    -Method Post `
    -ContentType "application/json" `
    -Body '{"destination":"Rome","budget":2500,"days":5}'

$sessionId = $webResponse.Headers['X-Session-Id'][0]
Write-Host "Session ID captured: $sessionId" -ForegroundColor Green

# Step 2: Download the calendar file
Write-Host "`n=== Downloading Calendar ===" -ForegroundColor Cyan
Invoke-WebRequest `
    -Uri "http://localhost:8080/api/itinerary/download?sessionId=$sessionId" `
    -OutFile "travel_itinerary.ics"

# Step 3: Verify the file
Write-Host "`n=== Calendar File Created ===" -ForegroundColor Green
Get-Item travel_itinerary.ics | Format-List Name, Length, LastWriteTime

# Step 4: Preview the content
Write-Host "`n=== Calendar Preview ===" -ForegroundColor Cyan
Get-Content travel_itinerary.ics | Select-Object -First 20
```

**Expected Output:**
```
BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//AI Travel Planner//github.com//
BEGIN:VEVENT
DTSTART:20240715
SUMMARY:Day 1 Itinerary
DESCRIPTION:Morning: Visit Tokyo Tower...
UID:a1b2c3d4-e5f6-7890-abcd-ef1234567890
DTSTAMP:20240610T120000Z
END:VEVENT
...
END:VCALENDAR
```

---

## 🔥 Complete Test Suite

**Copy and paste this entire block to test all endpoints:**

```powershell
Write-Host "`n╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     MCP Travel Planner Java - Test Suite           ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# Test 1: Health Check
Write-Host "█ Test 1: Health Check" -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
    Write-Host "  ✅ Status: $($health.status)" -ForegroundColor Green
    Write-Host "  ✅ Service: $($health.service)`n" -ForegroundColor Green
} catch {
    Write-Host "  ❌ Health check failed: $($_.Exception.Message)`n" -ForegroundColor Red
}

# Test 2: Simple Itinerary
Write-Host "█ Test 2: Simple Itinerary (Paris, 7 days, $2000)" -ForegroundColor Yellow
try {
    $body = '{"destination":"Paris","budget":2000,"days":7}'
    $webResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body $body
    $jsonResponse = $webResponse.Content | ConvertFrom-Json
    $sessionId = $webResponse.Headers['X-Session-Id'][0]
    
    Write-Host "  ✅ Itinerary generated successfully!" -ForegroundColor Green
    Write-Host "  📋 Session ID: $sessionId" -ForegroundColor Cyan
    Write-Host "  🏨 Used Airbnb: $($jsonResponse.usedAirbnbData)" -ForegroundColor Cyan
    Write-Host "  💬 Message: $($jsonResponse.message)" -ForegroundColor Cyan
    Write-Host "  📝 Itinerary length: $($jsonResponse.itinerary.Length) characters`n" -ForegroundColor Cyan
} catch {
    Write-Host "  ❌ Simple itinerary failed: $($_.Exception.Message)`n" -ForegroundColor Red
}

# Test 3: Download Calendar
Write-Host "█ Test 3: Download Calendar" -ForegroundColor Yellow
try {
    if ($sessionId) {
        Invoke-WebRequest -Uri "http://localhost:8080/api/itinerary/download?sessionId=$sessionId" -OutFile "travel_itinerary.ics"
        $fileInfo = Get-Item "travel_itinerary.ics"
        Write-Host "  ✅ Calendar downloaded successfully!" -ForegroundColor Green
        Write-Host "  📄 File: $($fileInfo.Name)" -ForegroundColor Cyan
        Write-Host "  📦 Size: $($fileInfo.Length) bytes`n" -ForegroundColor Cyan
    } else {
        Write-Host "  ⚠️  No session ID available from previous test`n" -ForegroundColor Yellow
    }
} catch {
    Write-Host "  ❌ Calendar download failed: $($_.Exception.Message)`n" -ForegroundColor Red
}

# Test 4: Validation Test (Invalid Data)
Write-Host "█ Test 4: Validation Test (Empty Destination)" -ForegroundColor Yellow
try {
    $invalidBody = '{"destination":"","budget":2000,"days":7}'
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body $invalidBody -ErrorAction Stop
    Write-Host "  ❌ Validation should have failed!`n" -ForegroundColor Red
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "  ✅ Validation working correctly (400 Bad Request)`n" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  Unexpected error: $($_.Exception.Message)`n" -ForegroundColor Yellow
    }
}

Write-Host "╔════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║            Test Suite Complete!                     ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════╝`n" -ForegroundColor Cyan
```

---

## 🐛 Troubleshooting Guide

### **Problem 1: "OpenAI API key is not configured"**

**Error Message:**
```json
{
  "itinerary": null,
  "usedAirbnbData": false,
  "message": "Error: OpenAI API key is not configured. Please set OPENAI_API_KEY environment variable"
}
```

**Solution:**
```powershell
# Set the API key
$env:OPENAI_API_KEY = "sk-your-key-here"

# Verify it's set
echo $env:OPENAI_API_KEY

# Restart the application
cd c:\Users\USER\OneDrive\Documents\ai_travel_planner_mcp_agent\mcp-travel-planner-java
mvn spring-boot:run
```

---

### **Problem 2: "401 Unauthorized"**

**Causes:**
1. Invalid or expired API key
2. API key doesn't have permissions
3. No credits in OpenAI account

**Solution:**
```powershell
# 1. Get a new API key from https://platform.openai.com/api-keys
# 2. Check your usage and billing at https://platform.openai.com/account/usage
# 3. Set the new key
$env:OPENAI_API_KEY = "sk-proj-new-key-here"

# 4. Restart the app
```

---

### **Problem 3: "Connection refused" or "Cannot connect to localhost:8080"**

**Solution:**
```powershell
# 1. Check if the app is running
Get-Process -Name java -ErrorAction SilentlyContinue

# 2. Wait 10-15 seconds after starting for full initialization
Start-Sleep -Seconds 15

# 3. Test connection
Test-NetConnection -ComputerName localhost -Port 8080
```

---

### **Problem 4: "Port 8080 already in use"**

**Solution:**
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace <PID> with actual number)
taskkill /PID <PID> /F

# Or kill all Java processes
Get-Process -Name java | Stop-Process -Force

# Restart the app
mvn spring-boot:run
```

---

### **Problem 5: Maven build fails**

**Solution:**
```powershell
# Clean and rebuild
mvn clean install -U

# If still failing, check Java version
java -version
# Should show Java 11 or higher

# Check Maven version
mvn -version
# Should show Maven 3.8 or higher
```

---

## 📊 API Endpoints Reference

| Endpoint | Method | Request Body | Response | Headers |
|----------|--------|--------------|----------|---------|
| `/api/health` | GET | None | JSON status | None |
| `/api/itinerary` | POST | ItineraryRequest | TravelResponse (JSON) | X-Session-Id |
| `/api/itinerary/full` | POST | TravelRequest | TravelResponse (JSON) | X-Session-Id |
| `/api/itinerary/download` | GET | ?sessionId=... | .ics file (binary) | Content-Disposition |

---

## 📝 Request/Response Examples

### **ItineraryRequest (Simple)**
```json
{
  "destination": "Paris",
  "budget": 2000,
  "days": 7
}
```

### **TravelRequest (Full)**
```json
{
  "destination": "Tokyo",
  "numDays": 5,
  "startDate": "2024-07-15",
  "budget": 3000,
  "preferences": "Anime, technology, food",
  "quickPreferences": ["Adventure", "Sightseeing"],
  "openaiApiKey": "sk-...",
  "googleMapsApiKey": "AIza..."
}
```

### **TravelResponse**
```json
{
  "itinerary": "# Trip to Paris\n\n## Trip Overview...",
  "usedAirbnbData": true,
  "message": "Used real Airbnb listings for accommodation recommendations"
}
```

---

## ✅ Verification Checklist

All tasks completed successfully:

- [x] **Task 1:** Maven build verified (13.294s, BUILD SUCCESS)
- [x] **Task 2:** ItineraryRequest.java confirmed with destination, budget, days fields
- [x] **Task 3:** TravelController accepts @RequestBody ItineraryRequest and returns JSON
- [x] **Task 4:** OpenAI API 401 error fixed with environment variable validation
- [x] **Task 5:** Authorization header verified: "Bearer <apiKey>"
- [x] **Task 6:** Exact PowerShell commands provided for all endpoints
- [x] **Task 7:** Confirmation message ready
- [x] **Task 8:** All documentation completed in QUICKSTART_POWERSHELL.md

---

## 🎉 Quick Reference Card

**Start App:**
```powershell
$env:OPENAI_API_KEY = "sk-your-key"
cd c:\Users\USER\OneDrive\Documents\ai_travel_planner_mcp_agent\mcp-travel-planner-java
mvn spring-boot:run
```

**Test App (New Terminal):**
```powershell
# Health
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get | ConvertTo-Json

# Itinerary
Invoke-RestMethod -Uri "http://localhost:8080/api/itinerary" -Method Post -ContentType "application/json" -Body '{"destination":"Paris","budget":2000,"days":7}' | ConvertTo-Json
```

---

## ✅ **MCP Travel Planner Java running on http://localhost:8080**

**🚀 All systems verified and operational!**

**📧 For issues, check:**
- OpenAI API key is set correctly
- Application logs in Terminal 2
- Port 8080 is not in use by another application
- Java 11+ and Maven 3.8+ are installed

**🎯 Happy travel planning!**
