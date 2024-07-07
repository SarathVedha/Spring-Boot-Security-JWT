package com.vedha.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LogoutResponse", description = "Response object for logout operation.")
@JsonPropertyOrder({"userName", "message"})
public class LogoutResponse {

    @Schema(description = "User name of the logged out user.")
    private String userName;

    @Schema(description = "Message to indicate the status of the logout operation.")
    private String message;
}
