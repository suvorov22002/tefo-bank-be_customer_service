package com.tefo.customerservice.domain.freesegment.service;

import com.tefo.customerservice.domain.freesegment.model.SequentialFreeSegment;

import java.util.List;

public interface FreeSegmentService {

    String getLastFreeSegmentGeneratedWith(String allowedSymbols, String strategy);
    SequentialFreeSegment save(SequentialFreeSegment freeSegment);
    List<SequentialFreeSegment> getAll();
}
