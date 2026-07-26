# AI Interview Simulator

A production-ready AI-powered interview simulator built with **Google Agent Development Kit (ADK)**, **Java 21**, and **Spring Boot 3**. This application simulates realistic technical interviews and provides comprehensive performance evaluations.

## 🎯 Project Overview

The AI Interview Simulator is designed for technical students preparing for software engineering interviews. It processes technical study materials, creates a knowledge base using RAG (Retrieval-Augmented Generation), and conducts intelligent interviews with adaptive difficulty levels.

### Key Features

- **📄 PDF Processing**: Upload and extract technical content from PDFs
- **🔍 RAG Pipeline**: Semantic search with vector embeddings for accurate context retrieval
- **🤖 AI Interview Agent**: Realistic technical interviewer powered by Google Gemini
- **📊 Performance Evaluation**: Detailed reports with scores, feedback, and study recommendations
- **🔌 Modular Architecture**: Clean separation of concerns following SOLID principles
- **🚀 Future-Ready**: Voice interaction and multiple interview modes easily extensible

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| **Language** | Java 21 (LTS) |
| **Framework** | Spring Boot 3.2 |
| **AI/LLM** | Google Generative AI (Gemini) |
| **Vector DB** | PostgreSQL + pgvector |
| **PDF Processing** | Apache PDFBox 3.0 |
| **Build Tool** | Maven 3.9+ |

## 📋 Requirements

- Java 21 or later
- Maven 3.9+
- PostgreSQL 14+ with pgvector extension
- Google Gemini API key

## 🚀 Quick Start

### 1. Clone and Setup

```bash
git clone https://github.com/UnikPrince/Knowledge-Test.git
cd Knowledge-Test

# Copy environment template
cp .env.example .env
```

### 2. Configure Environment

Edit `.env` with your configuration:

```env
GOOGLE_API_KEY=your_actual_api_key
DB_HOST=localhost
DB_USER=postgres
DB_PASSWORD=your_db_password
```

### 3. Setup Database

```bash
# Create database
createdb interview_simulator

# Install pgvector extension
psql interview_simulator -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

### 4. Build and Run

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/interview/simulator/
│   │   ├── config/                 # Configuration classes
│   │   ├── domain/
│   │   │   ├── entities/          # JPA entities
│   │   │   ├── models/            # Domain models
│   │   │   └── enums/             # Enumerations
│   │   ├── application/
│   │   │   ├── dto/               # Data transfer objects
│   │   │   ├── services/          # Business logic
│   │   │   └── events/            # Application events
│   │   ├── infrastructure/
│   │   │   ├── pdf/               # PDF processing
│   │   │   ├── ai/                # AI/LLM integration
│   │   │   ├── vector/            # Vector store
│   │   │   └── persistence/       # Repositories
│   │   ├── presentation/
│   │   │   ├── controllers/       # REST controllers
│   │   │   └── advice/            # Exception handlers
│   │   └── InterviewSimulatorApplication.java
│   └── resources/
│       └── application.properties  # Spring config
└── test/
    └── java/com/interview/simulator/
```

## 🔄 Application Flow

### Phase 1: PDF Processing

1. User uploads PDF file(s)
2. Extract text content
3. Filter technical content (Java, Spring, DevOps, etc.)
4. Split into chunks with overlap
5. Store in database

### Phase 2: RAG Pipeline

1. Generate embeddings for document chunks
2. Store vectors in PostgreSQL with pgvector
3. Create semantic search index
4. Set up retrieval with similarity threshold

### Phase 3: Interview Execution

1. Initialize interview session
2. Retrieve relevant context from knowledge base
3. Generate initial question using RAG
4. Student provides answer
5. AI evaluates and asks follow-ups
6. Adapt difficulty dynamically
7. Continue for configured question count

### Phase 4: Performance Evaluation

1. Analyze all responses
2. Calculate technical knowledge score
3. Evaluate communication clarity
4. Assess problem-solving approach
5. Generate comprehensive report
6. Provide study recommendations

## 🔌 REST API Endpoints

### PDF Management
- `POST /api/documents/upload` - Upload PDF file
- `GET /api/documents` - List uploaded documents
- `DELETE /api/documents/{id}` - Delete document

### Interview Operations
- `POST /api/interviews/start` - Start new interview session
- `POST /api/interviews/{sessionId}/answer` - Submit answer to question
- `GET /api/interviews/{sessionId}` - Get interview session details
- `POST /api/interviews/{sessionId}/end` - End interview and generate report
- `GET /api/interviews/{sessionId}/report` - Get performance report

### Knowledge Base
- `GET /api/knowledge/search` - Semantic search with query
- `GET /api/knowledge/stats` - Knowledge base statistics

## 🧠 Core Components

### 1. PDF Processing Module (`infrastructure/pdf`)

- `PDFExtractor`: Text extraction from PDFs
- `ContentFilter`: Filter technical vs. non-technical content
- `TextChunker`: Split text with configurable overlap
- `PdfProcessingService`: Orchestrates PDF operations

### 2. RAG Module (`infrastructure/ai`)

- `EmbeddingService`: Generate embeddings using Google API
- `VectorStore`: PostgreSQL vector storage and retrieval
- `RagRetriever`: Semantic search and context retrieval
- `PromptEngineer`: Context-aware prompt construction

### 3. Interview Agent (`application/services`)

- `InterviewAgent`: Main interviewer logic
- `ConversationManager`: Track interview state
- `QuestionGenerator`: Generate adaptive questions
- `ResponseEvaluator`: Evaluate student answers

### 4. Evaluation Engine (`application/services`)

- `EvaluationService`: Score calculation
- `ReportGenerator`: Generate performance reports
- `StudyPlanner`: Recommend study topics

## ⚙️ Configuration

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Google AI
google.api.key=${GOOGLE_API_KEY}
google.model.id=${GOOGLE_MODEL_ID}

# Database
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update

# PDF
pdf.chunk-size=${PDF_CHUNK_SIZE:1000}
pdf.chunk-overlap=${PDF_CHUNK_OVERLAP:200}

# RAG
rag.similarity-threshold=${RAG_SIMILARITY_THRESHOLD:0.7}
rag.top-k-results=${RAG_TOP_K_RESULTS:5}

# Interview
interview.max-questions=${INTERVIEW_MAX_QUESTIONS:10}
interview.difficulty-adaptive=${INTERVIEW_DIFFICULTY_ADAPTIVE:true}
```

## 🧪 Testing

Run tests with:

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=InterviewAgentTest

# With coverage
mvn test jacoco:report
```

## 📊 Performance Metrics

- **PDF Processing**: ~1-2 seconds for 10-page document
- **Embedding Generation**: ~500ms per document (cached)
- **RAG Retrieval**: ~100-200ms per query
- **Interview Response**: ~2-5 seconds (LLM dependent)

## 🔐 Security Considerations

- ✅ API keys stored in environment variables (never hardcoded)
- ✅ Input validation on all endpoints
- ✅ SQL injection prevention via JPA
- ✅ Rate limiting on API endpoints
- ✅ CORS configured for trusted origins
- ✅ Logging excludes sensitive data

## 🚀 Future Enhancements

### Near-term
- [ ] Voice-based interviews with ADK Voice
- [ ] Resume parsing and analysis
- [ ] Company-specific interview modes
- [ ] Coding challenge integration

### Medium-term
- [ ] Multi-language support
- [ ] Multiple interviewer personalities
- [ ] Real-time performance analytics
- [ ] Web and mobile clients

### Long-term
- [ ] Interview session history
- [ ] User authentication & profiles
- [ ] Analytics dashboard
- [ ] Distributed training pipeline

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit pull request

## 📝 Code Standards

- Follow Google Java Style Guide
- Use Lombok for boilerplate reduction
- Add unit tests for new features (>80% coverage)
- Document public APIs with JavaDoc
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**UnikPrince**  
Senior Java Engineer | AI/ML Enthusiast

## 📞 Support & Contact

For issues, questions, or suggestions:
- 📧 Create an issue on GitHub
- 💬 Start a discussion
- 🔗 See the wiki for more documentation

---

**Last Updated**: July 2026  
**Status**: 🚧 In Active Development
