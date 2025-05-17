package org.example.speaknotebackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.speaknotebackend.domain.entity.AnnotationBlock;
import org.example.speaknotebackend.domain.repository.AnnotationRepository;
import org.example.speaknotebackend.dto.AnnotationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/annotations")
@RequiredArgsConstructor
public class AnnotationController {

    private final AnnotationRepository annotationRepository;

    @GetMapping
    public List<AnnotationBlock> getAnnotationsByPage(@RequestParam int page) {
        return annotationRepository.findByPageNumber(page);
    }

    @PostMapping("/api/annotations")
    public ResponseEntity<Void> saveAnnotations(@RequestBody List<AnnotationDto> annotations) {
        for (AnnotationDto dto : annotations) {
            AnnotationBlock block = AnnotationBlock.builder()
                    .refinedText(dto.getText())
                    .x(dto.getX())
                    .y(dto.getY())
                    .pageNumber(dto.getPageNumber())
                    .createdAt(LocalDateTime.now())
                    .build();
            annotationRepository.save(block);
        }
        return ResponseEntity.ok().build();
    }

}

