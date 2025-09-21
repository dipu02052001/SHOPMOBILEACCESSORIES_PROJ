package com.example.catalog.service;

import com.example.catalog.domain.Cart;
import com.example.catalog.domain.CartItem;
import com.example.catalog.domain.CartItemDto;
import com.example.catalog.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCart(String userId) {
        return cartRepository.findById(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(userId);
            return cartRepository.save(cart);
        });
    }

    public void addToCart(String userId, CartItemDto itemDto) {
        Cart cart = getCart(userId);
        Optional<CartItem> existing = cart.getItems().stream()
            .filter(i -> i.getProductId().equals(itemDto.getProductId()))
            .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + itemDto.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            cart.getItems().add(item);
        }
        cartRepository.save(cart);
    }

    public void removeFromCart(String userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}