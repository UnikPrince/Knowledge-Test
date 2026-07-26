package com.interview.simulator.infrastructure.pdf;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

/**
 * Service for extracting text content from PDF files.
 */
@Service
@Slf4j
public class PDFExtractor {

    /**
     * Extract text from a PDF file.
     *
     * @param pdfFile the PDF file to extract text from
     * @return extracted text content
     * @throws IOException if PDF cannot be read
     */
    public String extractText(File pdfFile) throws IOException {
        log.debug("Extracting text from PDF: {}", pdfFile.getName());
        
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            log.info("Successfully extracted text from PDF: {} (length: {})", pdfFile.getName(), text.length());
            return text;
        } catch (IOException e) {
            log.error("Error extracting text from PDF: {}", pdfFile.getName(), e);
            throw e;
        }
    }

    /**
     * Extract text from specific page range in a PDF.
     *
     * @param pdfFile the PDF file
     * @param startPage starting page number (1-indexed)
     * @param endPage ending page number (1-indexed)
     * @return extracted text from specified pages
     * @throws IOException if PDF cannot be read
     */
    public String extractTextFromPages(File pdfFile, int startPage, int endPage) throws IOException {
        log.debug("Extracting text from pages {}-{} in PDF: {}", startPage, endPage, pdfFile.getName());
        
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            return stripper.getText(document);
        } catch (IOException e) {
            log.error("Error extracting pages from PDF: {}", pdfFile.getName(), e);
            throw e;
        }
    }
}
