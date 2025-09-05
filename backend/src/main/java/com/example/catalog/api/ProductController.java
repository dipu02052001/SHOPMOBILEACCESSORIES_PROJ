package com.example.catalog.api;

// import com.example.catalog.api.dto.CreateProductRequest;
import com.example.catalog.api.dto.ProductResponse;
import com.example.catalog.domain.Product;
import com.example.catalog.repo.ProductRepository;
import com.example.catalog.service.FileStorageService;
// import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repo;
    private final FileStorageService storage;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    // ---------------- CREATE PRODUCT WITH IMAGE ----------------
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProductResponse> createProduct(
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("price") BigDecimal price,
        @RequestParam(value = "image", required = false) MultipartFile image
) {
    Product product = Product.builder()
            .name(name)
            .description(description)
            .price(price)
            .build();

    // Store image if uploaded
    if (image != null && !image.isEmpty()) {
        String storedFilename = storage.store(image);   // your storage service
        String publicUrl = baseUrl + "/uploads/" + storedFilename;
        product.setImageUrl(publicUrl);
    }

    product = repo.save(product);

    return ResponseEntity.created(URI.create("/api/products" + product.getId()))
            .body(toResponse(product));
}


    // ---------------- GET ALL ----------------
    @GetMapping("/get")
    public ResponseEntity<List<ProductResponse>> all() {
        List<ProductResponse> products = repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(products);
    }

    // ---------------- GET ONE ----------------
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> one(@PathVariable("id") Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found: " + id));
        return ResponseEntity.ok(toResponse(product));
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- HELPER ----------------
    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImageUrl()
        );
    }
}
