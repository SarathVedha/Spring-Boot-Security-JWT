package com.vedha.controller;

import com.vedha.entity.UsersEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/secure")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Secure", description = "Secure API")
public class SecureController {

    @Operation(summary = "Get the current user", description = "Get the current user")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsersEntity> me() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsersEntity user = (UsersEntity) authentication.getPrincipal();

        return ResponseEntity.ok(user);
    }
}
