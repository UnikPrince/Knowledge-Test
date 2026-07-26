package com.interview.simulator.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_chunks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(nullable = false)
    private Integer chunkIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "embedding", columnDefinition = "vector(768)")
    private String embedding;

    private Boolean isQuestion;

    private Boolean isTechnicalContent;

    @Column(columnDefinition = "TEXT")
    private String metadata;
}
