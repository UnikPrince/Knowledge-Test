package com.interview.simulator.presentation.controllers;

import com.interview.simulator.infrastructure.pdf.PdfProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/documents")
@Slf4j
@CrossOrigin(origins = {"localhost:3000", "localhost:3001"})
public class DocumentController {

    @Autowired
    private PdfProcessingService pdfProcessingService;

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * Upload and process PDF document.
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Processing PDF upload: {}", file.getOriginalFilename());
            
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("File size exceeds 50MB limit"));
            }
            
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Only PDF files are supported"));
            }
            
            // Save file
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);
            
            File tempFile = new File(uploadPath.toFile(), file.getOriginalFilename());
            file.transferTo(tempFile);
            
            // Process PDF
            var document = pdfProcessingService.processPdf(tempFile);
            
            return ResponseEntity.ok(new DocumentUploadResponse(
                    document.getId(),
                    document.getFileName(),
                    document.getStatus().toString(),
                    document.getTotalChunks()
            ));
        } catch (Exception e) {
            log.error("Error uploading document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error processing document"));
        }
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class DocumentUploadResponse {
        private Long documentId;
        private String fileName;
        private String status;
        private Integer chunks;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ErrorResponse {
        private String error;
    }
}
