package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.dto.CartDTO;
import com.example.ecommerce_tut.dto.OrderDTO;
import com.example.ecommerce_tut.exception.ResourceNotFoundException;
import com.example.ecommerce_tut.mapper.CartMapper;
import com.example.ecommerce_tut.mapper.OrderMapper;
import com.example.ecommerce_tut.model.*;
import com.example.ecommerce_tut.repository.OrderRepository;
import com.example.ecommerce_tut.repository.ProductRepository;
import com.example.ecommerce_tut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
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
    private final EmailService emailService;

    public OrderDTO createOrder(Long userId, String address, String phone) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        CartDTO cartDTO = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDTO);
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);

        List<OrderItem> creatOrderItems = createOrderItems(order, cart);
        order.setItems(creatOrderItems);

        try {
            emailService.sendOrderConfirmationMail(order);
        }catch (MailException e){
            logger.error(e.getMessage());
        }
        return orderMapper.toDto(orderRepository.save(order));
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(item -> {
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
            if (product.getQuantity() == null) {
                throw new IllegalStateException("Product Quantity Not Found");
            }
            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Product Quantity Not Found");
            }
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
            return new OrderItem(null, order, product, item.getQuantity(), product.getPrice());
        }).toList();
    }

    public List<OrderDTO> getAllOrders() {
        return this.orderMapper.toDTOs(orderRepository.findAll());
    }

    public List<OrderDTO> getUserOrders(Long userId) {
        return this.orderMapper.toDTOs(orderRepository.findByUserId(userId));
    }

    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }
}

