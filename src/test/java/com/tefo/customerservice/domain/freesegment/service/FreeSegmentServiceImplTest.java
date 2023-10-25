package com.tefo.customerservice.domain.freesegment.service;

import com.tefo.customerservice.domain.freesegment.model.SequentialFreeSegment;
import com.tefo.customerservice.domain.freesegment.repository.FreeSegmentRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class FreeSegmentServiceImplTest {

    @Mock
    private FreeSegmentRepository freeSegmentRepository;
    @InjectMocks
    private FreeSegmentServiceImpl freeSegmentService;

    private SequentialFreeSegment segment;

    @BeforeEach
    void setUp() {
        segment = Instancio.create(SequentialFreeSegment.class);
        segment.setFreeSegment("freeSegment");
    }

    @Test
    @DisplayName("Should return last free segments generated with given strategy and allow symbols")
    void shouldReturnLastFreeSegmentGeneratedWith() {
        String allowSymbols = "allowSymbols";
        String strategy = "strategy";

        when(freeSegmentRepository.findFirstByAllowedSymbolsAndStrategyOrderByGeneratedAtDesc(allowSymbols, strategy)).thenReturn(Optional.of(segment));

        assertEquals(freeSegmentService.getLastFreeSegmentGeneratedWith(allowSymbols, strategy), segment.getFreeSegment());

        verify(freeSegmentRepository).findFirstByAllowedSymbolsAndStrategyOrderByGeneratedAtDesc(anyString(), anyString());
    }

    @Test
    @DisplayName("Should return null when no free segments generated with given strategy and allow symbols")
    void shouldReturnNullWhenGetLastSegment() {
        String allowSymbols = "allowSymbols";
        String strategy = "strategy";

        when(freeSegmentRepository.findFirstByAllowedSymbolsAndStrategyOrderByGeneratedAtDesc(allowSymbols, strategy)).thenReturn(Optional.empty());

        assertNull(freeSegmentService.getLastFreeSegmentGeneratedWith(allowSymbols, strategy));

        verify(freeSegmentRepository).findFirstByAllowedSymbolsAndStrategyOrderByGeneratedAtDesc(anyString(), anyString());
    }

    @Test
    @DisplayName("Should save free segments and trigger injected repository")
    void shouldSaveFreeSegment() {
        when(freeSegmentRepository.save(segment)).thenReturn(segment);

        assertEquals(freeSegmentService.save(segment), segment);

        verify(freeSegmentRepository).save(any(SequentialFreeSegment.class));
    }

    @Test
    @DisplayName("Should return all free segments and trigger injected repository")
    void shouldReturnAllFreeSegments() {
        when(freeSegmentRepository.findAll()).thenReturn(List.of(segment));

        assertEquals(freeSegmentService.getAll(), List.of(segment));

        verify(freeSegmentRepository).findAll();
    }
}