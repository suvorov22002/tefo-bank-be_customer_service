package com.tefo.customerservice.domain.freesegment.service;

import com.tefo.customerservice.domain.freesegment.model.SequentialFreeSegment;
import com.tefo.customerservice.domain.freesegment.repository.FreeSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FreeSegmentServiceImpl implements FreeSegmentService {

    private final FreeSegmentRepository freeSegmentRepository;

    @Override
    public String getLastFreeSegmentGeneratedWith(String allowedSymbols, String strategy) {
        return freeSegmentRepository.findFirstByAllowedSymbolsAndStrategyOrderByGeneratedAtDesc(allowedSymbols, strategy)
                .map(SequentialFreeSegment::getFreeSegment).orElse(null);
    }

    @Override
    public SequentialFreeSegment save(SequentialFreeSegment freeSegment) {
        return freeSegmentRepository.save(freeSegment);
    }

    @Override
    public List<SequentialFreeSegment> getAll() {
        return freeSegmentRepository.findAll();
    }
}
