package com.tefo.customerservice.core.feginclient.documentservice;

import com.tefo.customerservice.core.model.DocumentResponse;
import com.tefo.customerservice.core.dto.CreateOnlyDocumentRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "documentService", url = "http://" + "${document.service.host}" + "/api", configuration = {DocumentFeignClientErrorDecoder.class})
public interface DocumentServiceClient {

    @PostMapping("/documents")
    DocumentResponse createOnlyDocument(@RequestBody @Valid CreateOnlyDocumentRequest request);

    @PostMapping(value = "/documents/{id}/files/single/by/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    DocumentResponse uploadSingleFile(@PathVariable String id, @RequestPart("file") MultipartFile file, @PathVariable String userId);

    @GetMapping("/documents/{id}")
    DocumentResponse getDocument(@PathVariable String id);

    @DeleteMapping("/documents/{id}/files/{fileId}/by/{userId}")
    DocumentResponse removeFile(@PathVariable String id, @PathVariable String fileId, @PathVariable String userId);
}
