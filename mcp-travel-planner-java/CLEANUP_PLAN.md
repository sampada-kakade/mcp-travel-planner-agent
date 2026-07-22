# Project Cleanup Plan

## ✅ Files to KEEP (Valid Java Project Files)

### In `mcp-travel-planner-java/`
- ✅ `src/main/java/com/mcp/travel/` → All Java source files
- ✅ `src/main/resources/application.properties` → Configuration
- ✅ `src/test/java/` → Test directory (empty but valid)
- ✅ `pom.xml` → Maven configuration with all dependencies
- ✅ `README.md` → Java-specific documentation
- ✅ `BUILD.md` → Build instructions
- ✅ `PROJECT_SUMMARY.md` → Project documentation
- ✅ `QUICKSTART.md` → Quick reference
- ✅ `.gitignore` → Git configuration

## ❌ Files to DELETE (Garbage/Outdated)

### Python-related files (in root directory):
- ❌ `app.py` → Old Python Streamlit application
- ❌ `requirements.txt` → Python dependencies
- ❌ `README.md` (root) → Python-specific documentation

### Old Java scaffold:
- ❌ `ai_travel_planner_mcp_agent/` → Entire directory (auto-generated scaffold)
  - Contains old/unused Java code
  - Has outdated pom.xml
  - Target directory with build artifacts

### Build artifacts (in `mcp-travel-planner-java/`):
- ❌ `target/` → Maven build output (regenerated on build)

### GitHub workflows (if Python-specific):
- ⚠️ `.github/modernize/java-upgrade/` → Review and decide

## 📋 Action Items

1. **Delete Python files from root:**
   - `app.py`
   - `requirements.txt`

2. **Delete old Java scaffold:**
   - `ai_travel_planner_mcp_agent/` (entire directory)

3. **Update root README.md:**
   - Replace with pointer to `mcp-travel-planner-java/README.md`
   - Or copy the Java README to root and delete Python content

4. **Clean build artifacts:**
   - Delete `mcp-travel-planner-java/target/`
   - (Optional - will be regenerated on next build)

5. **Review .github workflows:**
   - Keep only if they're for Java builds
   - Delete if they're for Python or outdated Java modernization

## 🎯 Final Structure (After Cleanup)

```
ai_travel_planner_mcp_agent/
├── mcp-travel-planner-java/          ← Main Java project
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mcp/travel/  ← Java source
│   │   │   └── resources/            ← Config files
│   │   └── test/java/                ← Tests
│   ├── pom.xml                       ← Maven config
│   ├── README.md                     ← Documentation
│   ├── BUILD.md
│   ├── PROJECT_SUMMARY.md
│   ├── QUICKSTART.md
│   └── .gitignore
└── README.md (root)                  ← Updated pointer or copy

DELETED:
- app.py
- requirements.txt  
- ai_travel_planner_mcp_agent/ directory
- target/ directories
```

## ✨ Benefits After Cleanup

1. ✅ No confusion between Python and Java versions
2. ✅ Clean project structure
3. ✅ Reduced disk space
4. ✅ Clear documentation
5. ✅ Only Java build artifacts
6. ✅ Single source of truth

## 🔍 Verification Steps

After cleanup, verify:
1. `mvn clean compile` works
2. `mvn spring-boot:run` starts the application
3. API endpoints respond correctly
4. No Python references remain
5. Documentation is Java-specific only
