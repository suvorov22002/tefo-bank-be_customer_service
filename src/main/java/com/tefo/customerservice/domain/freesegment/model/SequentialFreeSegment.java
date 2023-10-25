package com.tefo.customerservice.domain.freesegment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldNameConstants
@NoArgsConstructor
@Document(collection = "seq_free_segment")
public class SequentialFreeSegment {
    @Id
    private String id;
    private String freeSegment;
    private String strategy;
    private String allowedSymbols;
    private LocalDateTime generatedAt;
}
