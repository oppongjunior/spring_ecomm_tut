package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.dto.CartDTO;
import com.example.ecommerce_tut.dto.OrderDTO;
import com.example.ecommerce_tut.exception.ResourceNotFoundException;
import com.example.ecommerce_tut.mapper.CartMapper;
import com.example.ecommerce_tut.mapper.OrderMapper;
import com.example.ecommerce_tut.model.*;
import com.example.ecommerce_tut.repository.CartRepository;
import com.example.ecommerce_tut.repository.OrderRepository;
import com.example.ecommerce_tut.repository.ProductRepository;
import com.example.ecommerce_tut.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final CartService cartService;

    @Transactional
    public OrderDTO createOrder(Long userId, String address, String phoneNumber) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        CartDTO cartDTO = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDTO);
        if (cartDTO.getItems().isEmpty()) throw new IllegalStateException("Cannot create an order with an empty cart");
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        return orderMapper.toDto(savedOrder);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
            if (product.getQuantity() == null) {
                throw new IllegalStateException("Product quantity is not set for product");
            }
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for products");
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return new OrderItem(null, order, product, cartItem.getQuantity(), product.getPrice());
        }).toList();
    }

}

