package org.example.speaknotebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.speaknotebackend.domain.entity.AnnotationBlock;
import org.example.speaknotebackend.domain.repository.AnnotationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnnotationService {

    private final AnnotationRepository annotationRepository;

    public void saveAnnotation(String sessionId, String original, String refined) {
        AnnotationBlock block = AnnotationBlock.builder()
                .sessionId(sessionId)
                .originalText(original)
                .refinedText(refined)
                .createdAt(LocalDateTime.now())
                .build();

        annotationRepository.save(block);
    }
}