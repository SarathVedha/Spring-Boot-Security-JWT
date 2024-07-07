package com.vedha.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin API")
public class AdminController {

    @Operation(summary = "Get Admin", description = "Get Admin")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "get", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Map<String, String>> getAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello User Get API!"));
    }

    @Operation(summary = "Post Admin", description = "Post Admin")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping(value = "post", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Map<String, String>> postAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello Admin Post API!"));
    }

    @Operation(summary = "Put Admin", description = "Put Admin")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PutMapping(value = "put", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Map<String, String>> putAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello Admin Put API!"));
    }

    @Hidden
    @Operation(summary = "Delete Admin", description = "Delete Admin")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @DeleteMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Map<String, String>> deleteAdmin() {

        return ResponseEntity.ok(Map.of("message", "Hello Admin Delete API!"));
    }
}
