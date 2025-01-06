package com.example.ecommerce_tut.controller;

import com.example.ecommerce_tut.dto.OrderDTO;
import com.example.ecommerce_tut.model.OrderStatus;
import com.example.ecommerce_tut.model.User;
import com.example.ecommerce_tut.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String address, @RequestParam String phoneNumber) {
        User user = (User) userDetails;
        Long userId = user.getId();
        return ResponseEntity.ok(orderService.createOrder(userId, address, phoneNumber));
    }

    @GetMapping
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = (User) userDetails;
        Long userId = user.getId();
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
