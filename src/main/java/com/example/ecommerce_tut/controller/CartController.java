package com.example.ecommerce_tut.controller;

import com.example.ecommerce_tut.dto.CartDTO;
import com.example.ecommerce_tut.model.User;
import com.example.ecommerce_tut.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> addCart(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam Long productId,
                                           @RequestParam Integer quantity
    ) {
        User user = (User) userDetails;
        Long userId = user.getId();
        CartDTO cartDTO = cartService.addCart(productId, userId, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> getCarts(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Long userId = user.getId();
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        cartService.clearCart(user.getId());
        return ResponseEntity.noContent().build();
    }
}
