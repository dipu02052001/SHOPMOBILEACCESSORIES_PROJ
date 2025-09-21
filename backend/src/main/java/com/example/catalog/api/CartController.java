package com.example.catalog.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    // Simple in-memory cart storage: userId -> List of productIds
    private final Map<String, List<Long>> userCarts = new HashMap<>();

    // Add product to cart
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam String userId, @RequestParam Long productId) {
        userCarts.computeIfAbsent(userId, k -> new ArrayList<>()).add(productId);
        return ResponseEntity.ok("Product added to cart");
    }

    // Get cart items
    @GetMapping
    public ResponseEntity<List<Long>> getCart(@RequestParam String userId) {
        List<Long> cart = userCarts.getOrDefault(userId, new ArrayList<>());
        return ResponseEntity.ok(cart);
    }

    // Remove product from cart
    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam String userId, @RequestParam Long productId) {
        List<Long> cart = userCarts.getOrDefault(userId, new ArrayList<>());
        cart.remove(productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    // Clear cart
    @PostMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam String userId) {
        userCarts.remove(userId);
        return ResponseEntity.ok("Cart cleared");
    }
}
