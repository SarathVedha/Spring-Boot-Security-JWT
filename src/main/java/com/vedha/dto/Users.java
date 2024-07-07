package com.vedha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vedha.util.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Users", description = "Users DTO")
public class Users {

    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User Name", example = "Vedha")
    @NotBlank(message = "Name should not be empty")
    @Size(min = 3, max = 50, message = "Name should be between 3 and 50 characters")
    private String name;

    @Schema(description = "User Email", example = "vedha@gmail.com")
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User Password", example = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password should not be empty")
    @Size(min = 8, max = 50, message = "Password should be between 8 and 50 characters")
    private String userPassword;

    @Schema(description = "User Role", example = "ADMIN", accessMode = Schema.AccessMode.READ_ONLY)
    private Roles role;
}
