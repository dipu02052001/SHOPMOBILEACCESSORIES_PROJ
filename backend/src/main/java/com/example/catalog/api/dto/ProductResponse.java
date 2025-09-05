package com.example.catalog.api.dto;

import java.math.BigDecimal;

public record ProductResponse(
  Long id,
  String name,
  String description,
  BigDecimal price,
  String imageUrl
) {}
