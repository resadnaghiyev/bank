package com.rashad.bank.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "required should not be empty")
    @Schema(example = "resad")
    private String pin;

    @NotBlank(message = "required should not be empty")
    @Schema(example = "Resad123")
    private String password;
}

