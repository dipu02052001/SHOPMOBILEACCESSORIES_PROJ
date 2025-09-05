package com.example.catalog.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "products")
public class Product {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(length = 2000)
  private String description;

  @Column(precision = 12, scale = 2, nullable = false)
  private BigDecimal price;

  private String imageUrl; // served at /files/**

  private Instant createdAt;
  private Instant updatedAt;

  @PrePersist void prePersist(){ createdAt = Instant.now(); updatedAt = Instant.now(); }
  @PreUpdate  void preUpdate(){ updatedAt = Instant.now(); }
}

