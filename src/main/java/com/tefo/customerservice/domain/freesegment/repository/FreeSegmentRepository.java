package com.tefo.customerservice.domain.freesegment.repository;

import com.tefo.customerservice.domain.freesegment.model.SequentialFreeSegment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeSegmentRepository extends MongoRepository<SequentialFreeSegment, String> {

    Optional<SequentialFreeSegment> findFirstByAllowedSymbolsAndStrategyOrderByGeneratedAtDesc(String allowedSymbols, String strategy);
}
