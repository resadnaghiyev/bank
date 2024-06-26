package com.rashad.bank.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddAccountRequest {

    @NotBlank(message = "required-field")
    @Schema(example = "12345678")
    private String accountNumber;

    @NotNull(message = "required-field")
    @DecimalMin(value = "0.0", message = "greater-than-zero")
    @Schema(example = "100.0")
    private BigDecimal balance;
}
