# AI Interview Simulator

## Overview

Production-ready AI-powered interview simulator using **Google Agent Development Kit (ADK)** with Java and Spring Boot. This application provides intelligent interview experiences with adaptive difficulty, semantic search through knowledge bases, and comprehensive performance analysis.

### Key Features

- 🤖 **AI Interview Agent**: Google Gemini-powered intelligent interviewer
- 📄 **PDF Processing**: Extract and parse interview materials automatically
- 🔍 **RAG Pipeline**: Semantic search with vector embeddings for contextual questions
- 📊 **Performance Evaluation**: Comprehensive interview analysis and reporting
- 🎯 **Adaptive Difficulty**: Real-time difficulty adjustment based on responses
- 📱 **REST API**: Fully documented REST endpoints for frontend integration
- 🔐 **Enterprise Ready**: Spring Security, transaction management, error handling

## Architecture

```
┌─────────────────────────────────────────────┐
│           Frontend (React/Vue)              │
└────────────────┬────────────────────────────┘
                 │ HTTP/REST
┌────────────────▼────────────────────────────┐
│         API Controllers (Spring)            │
├─────────────────────────────────────────────┤
│  InterviewController | ReportController     │
│  DocumentController  | HealthController     │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│       Application Services Layer            │
├─────────────────────────────────────────────┤
│  InterviewAgent | QuestionGenerator         │
│  ResponseEvaluator | ConversationManager    │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│     Infrastructure Services Layer           │
├─────────────────────────────────────────────┤
│  RAG Pipeline:                              │
│  ├─ EmbeddingService (Google AI)            │
│  ├─ VectorStore (PostgreSQL pgvector)       │
│  ├─ RagRetriever                            │
│  └─ PromptEngineer                          │
│                                             │
│  PDF Processing:                            │
│  ├─ PDFExtractor (PDFBox)                   │
│  ├─ ContentFilter                           │
│  └─ TextChunker                             │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│      Data Persistence Layer (JPA)           │
├─────────────────────────────────────────────┤
│  PostgreSQL Database with pgvector          │
│  Tables: Documents, Interviews, Reports     │
└─────────────────────────────────────────────┘
```

## Technology Stack

- **Runtime**: Java 21
- **Framework**: Spring Boot 3.3.0
- **ORM**: Hibernate JPA
- **Database**: PostgreSQL with pgvector
- **AI/LLM**: Google Generative AI (Gemini)
- **PDF Processing**: Apache PDFBox
- **Build**: Maven
- **Logging**: SLF4J with Logback
- **Configuration**: Externalized config + .env files

## Project Structure

```
src/
├── main/
│   ├── java/com/interview/simulator/
│   │   ├── InterviewSimulatorApplication.java
│   │   ├── config/
│   │   │   ├── ApplicationConfig.java
│   │   │   ├── GoogleAiConfig.java
│   │   │   ├── DatabaseConfig.java
│   │   │   └── WebConfig.java
│   │   ├── domain/
│   │   │   └── entities/
│   │   │       ├── Document.java
│   │   │       ├── DocumentChunk.java
│   │   │       ├── InterviewSession.java
│   │   │       ├── InterviewExchange.java
│   │   │       └── PerformanceReport.java
│   │   ├── infrastructure/
│   │   │   ├── persistence/
│   │   │   │   ├── DocumentRepository.java
│   │   │   │   ├── DocumentChunkRepository.java
│   │   │   │   ├── InterviewSessionRepository.java
│   │   │   │   ├── InterviewExchangeRepository.java
│   │   │   │   └── PerformanceReportRepository.java
│   │   │   ├── pdf/
│   │   │   │   ├── PDFExtractor.java
│   │   │   │   ├── ContentFilter.java
│   │   │   │   ├── TextChunker.java
│   │   │   │   └── PdfProcessingService.java
│   │   │   └── ai/
│   │   │       ├── EmbeddingService.java
│   │   │       ├── VectorStore.java
│   │   │       ├── RagRetriever.java
│   │   │       └── PromptEngineer.java
│   │   ├── application/
│   │   │   └── services/
│   │   │       ├── InterviewAgent.java
│   │   │       ├── QuestionGenerator.java
│   │   │       ├── ResponseEvaluator.java
│   │   │       ├── ConversationManager.java
│   │   │       └── InterviewSessionService.java
│   │   ├── presentation/
│   │   │   ├── controllers/
│   │   │   │   ├── InterviewController.java
│   │   │   │   ├── ReportController.java
│   │   │   │   ├── DocumentController.java
│   │   │   │   └── HealthController.java
│   │   │   ├── dto/
│   │   │   │   ├── StartInterviewRequest.java
│   │   │   │   ├── SubmitAnswerRequest.java
│   │   │   │   ├── InterviewResponse.java
│   │   │   │   ├── EvaluationResponse.java
│   │   │   │   └── PerformanceReportDTO.java
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.properties
└── test/
```

## Setup Instructions

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- PostgreSQL 14+
- Google API Key (for Gemini)

### 1. Clone Repository

```bash
git clone https://github.com/UnikPrince/Knowledge-Test.git
cd Knowledge-Test
git checkout develop
```

### 2. Environment Configuration

Create `.env` file in project root:

```bash
# Google AI Configuration
GOOGLE_API_KEY=your_google_api_key_here
GOOGLE_MODEL_ID=gemini-2.0-flash

# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=interview_simulator
DB_USER=postgres
DB_PASSWORD=your_password

# Server Configuration
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api

# PDF Processing
PDF_CHUNK_SIZE=1000
PDF_CHUNK_OVERLAP=200
PDF_MAX_FILE_SIZE_MB=50

# RAG Configuration
RAG_SIMILARITY_THRESHOLD=0.7
RAG_TOP_K_RESULTS=5
EMBEDDING_MODEL=textembedding-gecko

# Interview Configuration
INTERVIEW_MAX_QUESTIONS=10
INTERVIEW_DIFFICULTY_ADAPTIVE=true
INTERVIEW_FOLLOW_UP_ENABLED=true

# Logging
LOG_LEVEL=INFO
LOG_FILE=logs/app.log
```

### 3. Database Setup

```bash
# Create database
creatodb interview_simulator

# Install pgvector extension
psql -d interview_simulator -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

### 4. Build and Run

```bash
# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# Or run the JAR directly
java -jar target/ai-interview-simulator-1.0.0.jar
```

The application will start at `http://localhost:8080/api`

## API Endpoints

### Interview Management

#### Start Interview
```bash
POST /api/v1/interviews/start
Content-Type: application/json

{
  "studentName": "John Doe",
  "topic": "Spring Boot Microservices",
  "maxQuestions": 10
}

Response:
{
  "sessionId": "uuid-here",
  "question": "What are the key benefits of using Spring Boot?",
  "questionsRemaining": 10,
  "status": "ACTIVE"
}
```

#### Submit Answer
```bash
POST /api/v1/interviews/{sessionId}/submit
Content-Type: application/json

{
  "answer": "Spring Boot provides rapid development..."
}

Response:
{
  "relevanceScore": 0.85,
  "clarityScore": 0.80,
  "completenessScore": 0.75,
  "answerQuality": "GOOD",
  "feedback": "Good answer. Consider providing more examples.",
  "nextQuestion": "Next question available"
}
```

#### Get Performance Report
```bash
GET /api/v1/reports/{sessionId}

Response:
{
  "overallScore": 78.5,
  "technicalKnowledge": 82.0,
  "conceptClarity": 75.0,
  "communicationSkills": 80.0,
  "confidenceLevel": 75.0,
  "strengthAreas": "...",
  "weakAreas": "...",
  "recommendations": "...",
  "readinessLevel": 4
}
```

### Document Management

#### Upload PDF
```bash
POST /api/v1/documents/upload
Content-Type: multipart/form-data

File: knowledge_base.pdf

Response:
{
  "documentId": 1,
  "fileName": "knowledge_base.pdf",
  "status": "PROCESSED",
  "chunks": 42
}
```

### Health Check
```bash
GET /api/v1/health

Response:
{
  "status": "UP",
  "message": "AI Interview Simulator is running"
}
```

## Development Workflow

### Running Tests

```bash
mvn test
```

### Code Quality

```bash
# Run checkstyle
mvn checkstyle:check

# Run spotbugs
mvn spotbugs:check
```

### Logging

Logs are written to `logs/app.log` and console. Configure in `application.properties`:

```properties
logging.level.com.interview.simulator=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
```

## Database Schema

### Documents Table
```sql
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    raw_content TEXT,
    extracted_content TEXT,
    total_chunks INT,
    uploaded_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP
);
```

### Interview Sessions Table
```sql
CREATE TABLE interview_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(36) UNIQUE NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    interview_topic VARCHAR(255),
    total_questions INT,
    questions_asked INT,
    current_difficulty VARCHAR(20),
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    notes TEXT,
    performance_report_id BIGINT
);
```

## Configuration

### Application Properties

Key configuration parameters in `application.properties`:

```properties
# RAG Parameters
rag.similarity-threshold=0.7  # Min similarity for retrieval
rag.top-k-results=5           # Top chunks to retrieve

# PDF Processing
pdf.chunk-size=1000           # Characters per chunk
pdf.chunk-overlap=200         # Overlap between chunks
pdf.max-file-size-mb=50       # Max file size

# Interview Settings
interview.max-questions=10           # Default questions
interview.difficulty-adaptive=true   # Enable difficulty adaptation
interview.follow-up-enabled=true     # Enable follow-up questions
```

## Performance Optimization

### Vector Search Optimization

```sql
-- Create index on embeddings for faster similarity search
CREATE INDEX ON document_chunks USING ivfflat (embedding vector_cosine_ops);
```

### Database Connection Pooling

HikariCP is configured automatically. Tune in `application.properties`:

```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

## Deployment

### Docker Build

```dockerfile
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/ai-interview-simulator-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      GOOGLE_API_KEY: ${GOOGLE_API_KEY}
      DB_HOST: postgres
    depends_on:
      - postgres

  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: interview_simulator
      POSTGRES_PASSWORD: password
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add new feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit pull request

## License

MIT License - see LICENSE file for details

## Support

For issues and questions:
- GitHub Issues: [Create Issue](https://github.com/UnikPrince/Knowledge-Test/issues)
- Email: support@interviewsimulator.dev

## Roadmap

- [ ] Real-time WebSocket support
- [ ] Multi-language interview support
- [ ] Video recording and analysis
- [ ] Peer comparison analytics
- [ ] Mobile app (React Native)
- [ ] Enterprise authentication (OAuth2/SAML)

---

**Developed by**: Prince Pandey  
**Last Updated**: 2026-07-26  
**Version**: 1.0.0
