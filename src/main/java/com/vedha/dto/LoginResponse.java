package com.vedha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginResponse", description = "Response object for login operation.")
@JsonPropertyOrder({"userName", "accessToken", "issuedDate", "expiryDate", "refreshToken", "refreshTokenIssuedDate", "refreshTokenExpiryDate", "message"})
public class LoginResponse {

    @Schema(description = "User name")
    private String userName;

    @Schema(description = "Jwt Access Token")
    private String accessToken;

    @Schema(description = "Issued date")
    @JsonProperty("accessTokenIssuedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issuedDate;

    @Schema(description = "Expiry date")
    @JsonProperty("accessTokenExpiryDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDate;

    @Schema(description = "Jwt Refresh Token")
    private String refreshToken;

    @Schema(description = "Refresh token issued date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenIssuedDate;

    @Schema(description = "Refresh token expiry date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenExpiryDate;

    @Schema(description = "Message", example = "Login successful")
    private String message;
}
