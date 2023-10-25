package com.tefo.customerservice.core.config;

import com.tefo.customerservice.core.feginclient.documentservice.DocumentFeignClientErrorDecoder;
import com.tefo.customerservice.core.feginclient.FeignClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public ErrorDecoder getFeignErrorDecoder() {
        return new FeignClientErrorDecoder();
    }

    @Bean
    public ErrorDecoder getDocumentFeignErrorDecoder() {
        return new DocumentFeignClientErrorDecoder();
    }
}
