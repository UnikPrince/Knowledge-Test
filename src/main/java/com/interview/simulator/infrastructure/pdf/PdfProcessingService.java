package com.interview.simulator.infrastructure.pdf;

import com.interview.simulator.domain.entities.Document;
import com.interview.simulator.domain.entities.DocumentChunk;
import com.interview.simulator.infrastructure.persistence.DocumentChunkRepository;
import com.interview.simulator.infrastructure.persistence.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Orchestrates PDF processing pipeline: extraction, filtering, and chunking.
 */
@Service
@Slf4j
public class PdfProcessingService {

    @Autowired
    private PDFExtractor pdfExtractor;

    @Autowired
    private ContentFilter contentFilter;

    @Autowired
    private TextChunker textChunker;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentChunkRepository documentChunkRepository;

    /**
     * Process a PDF file: extract, filter, and chunk content.
     */
    @Transactional
    public Document processPdf(File pdfFile) throws IOException {
        log.info("Starting PDF processing for: {}", pdfFile.getName());
        
        Document document = Document.builder()
                .fileName(pdfFile.getName())
                .filePath(pdfFile.getAbsolutePath())
                .fileSize(pdfFile.length())
                .status(Document.DocumentStatus.PROCESSING)
                .build();
        
        document = documentRepository.save(document);
        
        try {
            // Extract text
            String rawText = pdfExtractor.extractText(pdfFile);
            document.setRawContent(rawText);
            
            // Filter technical content
            String filteredContent = contentFilter.filterTechnicalContent(rawText);
            document.setExtractedContent(filteredContent);
            
            // Chunk text
            List<String> chunks = textChunker.chunkText(filteredContent);
            document.setTotalChunks(chunks.size());
            
            // Save chunks
            for (int i = 0; i < chunks.size(); i++) {
                DocumentChunk chunk = DocumentChunk.builder()
                        .document(document)
                        .chunkIndex(i)
                        .content(chunks.get(i))
                        .isTechnicalContent(true)
                        .isQuestion(detectQuestion(chunks.get(i)))
                        .build();
                documentChunkRepository.save(chunk);
            }
            
            document.setStatus(Document.DocumentStatus.PROCESSED);
            document.setProcessedAt(LocalDateTime.now());
            log.info("PDF processing completed: {} chunks created", chunks.size());
            
        } catch (Exception e) {
            document.setStatus(Document.DocumentStatus.FAILED);
            log.error("PDF processing failed for: {}", pdfFile.getName(), e);
        }
        
        return documentRepository.save(document);
    }

    /**
     * Detect if a chunk is a question.
     */
    private boolean detectQuestion(String text) {
        return text.trim().endsWith("?");
    }
}
