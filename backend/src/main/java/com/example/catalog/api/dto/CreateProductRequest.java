package com.example.catalog.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequest(
  @NotBlank String name,
  @Size(max = 2000) String description,
  @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price
) {}
