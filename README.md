# 🌍 MCP Travel Planner

A Java‑based travel recommendation service that suggests top destinations based on ratings.  
It includes a backend REST API and a simple frontend served on **localhost:8083**.

---

## 🚀 Features
- `/api/health` — Health check endpoint  
- `/api/destinations` — Lists all destinations  
- `/api/recommendations` — Shows top‑rated travel spots  
- Integrated frontend with live fetch and refresh button  

---

## 🧩 Tech Stack
- Java (Spring Boot)
- HTML, CSS, JavaScript (frontend)
- Maven build system
- VS Code for development

---

## 🖥️ How to Run Locally
1. Open the folder in VS Code.  
2. Run in terminal:
   ```bash
   cd mcp-travel-planner-java
   $env:SERVER_PORT="8083"
   java -jar .\target\mcp-travel-planner.jar
💡 Future Enhancements
Add user login and personalized recommendations

Integrate external travel APIs

Deploy to cloud (Azure or AWS)

🧑‍💻 Author
Sampada Kakade  
Created as part of the AI Travel Planner MCP Agent project.

Code

Once you save this file as `README.md`, run:
```bash
git add README.md
git commit -m "Added project README"
git push
