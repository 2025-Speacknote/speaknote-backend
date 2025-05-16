package org.example.speaknotebackend.domain.repository;

import org.example.speaknotebackend.domain.entity.AnnotationBlock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnnotationRepository extends MongoRepository<AnnotationBlock, String> {
}
