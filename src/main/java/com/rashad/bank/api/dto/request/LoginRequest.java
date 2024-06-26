package com.rashad.bank.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "required-field")
    @Schema(example = "12345")
    private String pin;

    @NotBlank(message = "required-field")
    @Schema(example = "Resad123")
    private String password;
}

