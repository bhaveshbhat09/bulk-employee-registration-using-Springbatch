package com.epfo.learn.web;

import com.epfo.learn.service.AppStorage;
import com.epfo.learn.service.BatchInfo;
import com.epfo.learn.service.BatchService;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private AppStorage storage;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BatchInfo> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(batchService.upload(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchInfo> status(@PathVariable long id) {
        BatchInfo info = batchService.getBatch(id);
        return info == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(info);
    }

    @GetMapping("/{id}/errors")
    public ResponseEntity<Resource> errors(@PathVariable long id) {
        Path errorFile = storage.errorFile(id);
        if (!Files.exists(errorFile)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"batch" + id + "-errors.txt\"")
                .body(new FileSystemResource(errorFile));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> badRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

        @PostMapping("/{id}/approve")
    public ResponseEntity<BatchInfo> approve(@PathVariable long id,
                                             @RequestParam(defaultValue = "tester") String approvedBy)
            throws Exception {
        BatchInfo info = batchService.approve(id, approvedBy);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping("/{id}/rejected-rows")
    public ResponseEntity<List<Map<String, Object>>> rejectedRows(@PathVariable long id) {
        if (batchService.getBatch(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(batchService.rejectedRows(id));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> conflict(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}