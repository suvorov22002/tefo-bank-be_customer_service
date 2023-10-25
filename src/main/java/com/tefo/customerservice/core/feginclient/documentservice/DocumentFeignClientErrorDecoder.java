package com.tefo.customerservice.core.feginclient.documentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tefo.customerservice.core.model.DocumentResponse;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class DocumentFeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        DocumentResponse exceptionDetails;
        try (InputStream body = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            exceptionDetails = mapper.readValue(body, DocumentResponse.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return new IllegalArgumentException(exceptionDetails.getMessage());
    }
}
