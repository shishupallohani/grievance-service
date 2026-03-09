package com.assist.grievance.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class DefaultController {

    private Map<String, Object> buildResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("module", "default");
        response.put("route", "/");
        response.put("timestamp", Instant.now().toString());
        return response;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> handleGet() {
        log.info("GET /api/default/test called");
        return ResponseEntity.ok(buildResponse());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> handlePost() {
        log.info("POST /api/default/test called");
        return ResponseEntity.ok(buildResponse());
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> handlePut() {
        log.info("PUT /api/default/test called");
        return ResponseEntity.ok(buildResponse());
    }

    @PatchMapping
    public ResponseEntity<Map<String, Object>> handlePatch() {
        log.info("PATCH /api/default/test called");
        return ResponseEntity.ok(buildResponse());
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> handleDelete() {
        log.info("DELETE /api/default/test called");
        return ResponseEntity.ok(buildResponse());
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Map<String, Object>> handleOptions() {
        log.info("OPTIONS /api/default/test called");
        return ResponseEntity.ok(buildResponse());
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Map<String, Object>> handleHead() {
        log.info("HEAD /api/default/test called");
        return ResponseEntity.ok().build();
    }

}
