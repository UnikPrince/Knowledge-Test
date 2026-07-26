-- Create pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Create documents table
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    raw_content TEXT,
    extracted_content TEXT,
    total_chunks INT,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_documents_status ON documents(status);
CREATE INDEX idx_documents_created_at ON documents(created_at);

-- Create document_chunks table
CREATE TABLE IF NOT EXISTS document_chunks (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    chunk_index INT NOT NULL,
    content TEXT NOT NULL,
    summary TEXT,
    embedding vector(768),
    is_question BOOLEAN,
    is_technical_content BOOLEAN,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_document_chunks_document_id ON document_chunks(document_id);
CREATE INDEX idx_document_chunks_is_question ON document_chunks(is_question);
CREATE INDEX idx_document_chunks_embedding ON document_chunks USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);

-- Create interview_sessions table
CREATE TABLE IF NOT EXISTS interview_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(36) UNIQUE NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    interview_topic VARCHAR(255),
    total_questions INT DEFAULT 10,
    questions_asked INT DEFAULT 0,
    current_difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    notes TEXT,
    performance_report_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_interview_sessions_session_id ON interview_sessions(session_id);
CREATE INDEX idx_interview_sessions_status ON interview_sessions(status);
CREATE INDEX idx_interview_sessions_student_name ON interview_sessions(student_name);

-- Create interview_exchanges table
CREATE TABLE IF NOT EXISTS interview_exchanges (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    exchange_index INT NOT NULL,
    question TEXT NOT NULL,
    student_answer TEXT,
    ai_evaluation TEXT,
    answer_quality VARCHAR(20),
    relevance_score NUMERIC(3, 2),
    clarity_score NUMERIC(3, 2),
    completeness_score NUMERIC(3, 2),
    feedback TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_interview_exchanges_session_id ON interview_exchanges(session_id);
CREATE INDEX idx_interview_exchanges_exchange_index ON interview_exchanges(exchange_index);

-- Create performance_reports table
CREATE TABLE IF NOT EXISTS performance_reports (
    id BIGSERIAL PRIMARY KEY,
    overall_score NUMERIC(5, 2) NOT NULL,
    technical_knowledge NUMERIC(5, 2) NOT NULL,
    concept_clarity NUMERIC(5, 2) NOT NULL,
    communication_skills NUMERIC(5, 2) NOT NULL,
    confidence_level NUMERIC(5, 2) NOT NULL,
    logical_reasoning NUMERIC(5, 2),
    problem_solving_ability NUMERIC(5, 2),
    answer_accuracy NUMERIC(5, 2),
    strength_areas TEXT,
    weak_areas TEXT,
    study_recommendations TEXT,
    personalized_feedback TEXT,
    estimated_readiness_level INT,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_performance_reports_overall_score ON performance_reports(overall_score);

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
