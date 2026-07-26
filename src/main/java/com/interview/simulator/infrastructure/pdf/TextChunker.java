package com.interview.simulator.infrastructure.pdf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service for splitting large text into smaller chunks with overlap.
 */
@Service
@Slf4j
public class TextChunker {

    @Value("${pdf.chunk-size:1000}")
    private int chunkSize;

    @Value("${pdf.chunk-overlap:200}")
    private int chunkOverlap;

    /**
     * Split text into chunks with configurable overlap.
     *
     * @param text text to chunk
     * @return list of text chunks
     */
    public List<String> chunkText(String text) {
        log.debug("Chunking text (size: {}, overlap: {})", chunkSize, chunkOverlap);
        
        List<String> chunks = new ArrayList<>();
        String[] sentences = text.split("(?<=[.!?])\\s+");
        
        StringBuilder currentChunk = new StringBuilder();
        List<String> sentenceBuffer = new ArrayList<>();
        
        for (String sentence : sentences) {
            sentenceBuffer.add(sentence);
            String combined = String.join(" ", sentenceBuffer);
            
            if (combined.length() >= chunkSize) {
                chunks.add(combined);
                
                // Keep overlap sentences
                int overlapSentences = 0;
                int overlapLength = 0;
                for (int i = sentenceBuffer.size() - 1; i >= 0 && overlapLength < chunkOverlap; i--) {
                    overlapLength += sentenceBuffer.get(i).length() + 1;
                    overlapSentences++;
                }
                
                sentenceBuffer = new ArrayList<>(sentenceBuffer.subList(
                    Math.max(0, sentenceBuffer.size() - overlapSentences),
                    sentenceBuffer.size()
                ));
            }
        }
        
        if (!sentenceBuffer.isEmpty()) {
            chunks.add(String.join(" ", sentenceBuffer));
        }
        
        log.info("Created {} chunks from text (original size: {})", chunks.size(), text.length());
        return chunks;
    }
}
