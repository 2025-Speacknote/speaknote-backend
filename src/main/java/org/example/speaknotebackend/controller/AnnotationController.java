package org.example.speaknotebackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.speaknotebackend.domain.entity.AnnotationBlock;
import org.example.speaknotebackend.domain.repository.AnnotationRepository;
import org.springframework.web.bind.annotation.*;

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
}

