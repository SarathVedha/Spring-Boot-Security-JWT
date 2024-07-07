package com.vedha.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Hidden
@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User API", description = "User API")
public class UserController {

    @Operation(summary = "Get User", description = "Get User API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getUser() {

        return ResponseEntity.ok(Map.of("message", "Hello User Get API!"));
    }

    @Operation(summary = "Post User", description = "Post User API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> postUser() {

        return ResponseEntity.ok(Map.of("message", "Hello User Post API!"));
    }

    @Operation(summary = "Put User", description = "Put User API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PutMapping(value = "/put", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> putUser() {

        return ResponseEntity.ok(Map.of("message", "Hello User Put API!"));
    }

    @Operation(summary = "Delete User", description = "Delete User API")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteUser() {

        return ResponseEntity.ok(Map.of("message", "Hello User Delete API!"));
    }
}
