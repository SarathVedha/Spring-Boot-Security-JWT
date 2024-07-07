package com.vedha.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/manager")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Manager", description = "Manager API")
public class ManagerController {

    @Operation(summary = "Get Manager API", description = "Get Manager API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getManager() {

        return ResponseEntity.ok(Map.of("message", "Hello Manager Get API!"));
    }

    @Operation(summary = "Post Manager API", description = "Post Manager API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> postAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello Manager Post API!"));
    }

    @Operation(summary = "Put Manager API", description = "Put Manager API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PutMapping(value = "/put", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> putAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello Manager Put API!"));
    }

    @Operation(summary = "Delete Manager API", description = "Delete Manager API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello Manager Delete API!"));
    }
}
