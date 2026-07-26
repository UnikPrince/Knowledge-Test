package com.interview.simulator.infrastructure.ai;

import com.interview.simulator.domain.entities.DocumentChunk;
import com.interview.simulator.infrastructure.persistence.DocumentChunkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Vector store service for semantic search using embeddings.
 * Handles storage and retrieval of document embeddings in PostgreSQL with pgvector.
 */
@Service
@Slf4j
public class VectorStore {

    @Autowired
    private DocumentChunkRepository documentChunkRepository;

    @Autowired
    private EmbeddingService embeddingService;

    /**
     * Store embedding for a document chunk.
     *
     * @param chunk document chunk
     * @param embedding embedding vector
     */
    public void storeEmbedding(DocumentChunk chunk, List<Float> embedding) {
        log.debug("Storing embedding for chunk: {}", chunk.getId());
        String embeddingStr = embedding.toString();
        chunk.setEmbedding(embeddingStr);
        documentChunkRepository.save(chunk);
    }

    /**
     * Semantic search: find similar chunks based on query embedding.
     *
     * @param queryEmbedding embedding of query text
     * @param topK number of results to return
     * @return list of similar document chunks
     */
    public List<DocumentChunk> semanticSearch(List<Float> queryEmbedding, int topK) {
        log.debug("Performing semantic search with top K: {}", topK);
        
        // In production, use pgvector similarity search:
        // SELECT * FROM document_chunks 
        // ORDER BY embedding <-> query_embedding LIMIT topK
        
        // For now, return all chunks (placeholder)
        return documentChunkRepository.findAll().stream()
                .limit(topK)
                .toList();
    }

    /**
     * Find similar chunks for a given text query.
     *
     * @param query text query
     * @param topK number of results
     * @return list of similar chunks
     */
    public List<DocumentChunk> findSimilar(String query, int topK) {
        log.debug("Finding similar chunks for query (length: {})", query.length());
        List<Float> queryEmbedding = embeddingService.generateEmbedding(query);
        return semanticSearch(queryEmbedding, topK);
    }
}
