package com.interview.simulator.infrastructure.ai;

import com.interview.simulator.domain.entities.DocumentChunk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * RAG (Retrieval-Augmented Generation) Retriever.
 * Retrieves relevant context from knowledge base for prompt augmentation.
 */
@Service
@Slf4j
public class RagRetriever {

    @Autowired
    private VectorStore vectorStore;

    @Value("${rag.top-k-results:5}")
    private int topKResults;

    @Value("${rag.similarity-threshold:0.7}")
    private double similarityThreshold;

    /**
     * Retrieve relevant context for a query.
     *
     * @param query query text
     * @return list of relevant document chunks
     */
    public List<DocumentChunk> retrieveContext(String query) {
        log.debug("Retrieving context for query: {}", query);
        List<DocumentChunk> results = vectorStore.findSimilar(query, topKResults);
        log.info("Retrieved {} relevant chunks", results.size());
        return results;
    }

    /**
     * Retrieve context with custom top K value.
     *
     * @param query query text
     * @param customTopK custom number of results
     * @return list of relevant document chunks
     */
    public List<DocumentChunk> retrieveContext(String query, int customTopK) {
        log.debug("Retrieving context with custom topK: {}", customTopK);
        return vectorStore.findSimilar(query, customTopK);
    }

    /**
     * Build context string from retrieved chunks.
     *
     * @param chunks retrieved document chunks
     * @return formatted context string
     */
    public String buildContextString(List<DocumentChunk> chunks) {
        log.debug("Building context string from {} chunks", chunks.size());
        
        StringBuilder context = new StringBuilder();
        context.append("RELEVANT CONTEXT:\n");
        context.append("=".repeat(50)).append("\n");
        
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            context.append("\n[Chunk ").append(i + 1).append("]\n");
            context.append(chunk.getContent()).append("\n");
        }
        
        context.append("\n").append("=".repeat(50)).append("\n");
        return context.toString();
    }
}
