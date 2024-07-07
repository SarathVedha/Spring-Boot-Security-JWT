package com.vedha.controller;

import com.vedha.dto.Login;
import com.vedha.dto.LoginResponse;
import com.vedha.dto.LogoutResponse;
import com.vedha.dto.Users;
import com.vedha.service.AuthService;
import com.vedha.util.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register", description = "Register a new user")
    @ApiResponse(responseCode = "201", description = "HTTP Status 201 Created")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> registerUser(@RequestBody @Valid Users user,
                                              @Parameter(description = "Role of user", example = "USER", required = true)
                                              @RequestParam(value = "role", defaultValue = "USER") Roles role) {

        Users build = Users.builder().name(user.getName()).email(user.getEmail()).userPassword(user.getUserPassword()).role(role).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(build));
    }

    @Operation(summary = "Login", description = "Login a user")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid Login login) {

        return ResponseEntity.ok(authService.login(login));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Refresh Token", description = "Refresh a user token")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(authService.refreshToken(request, response));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Logout", description = "Logout a user")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LogoutResponse> logoutUser(HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(authService.logout(request, response));
    }

}
