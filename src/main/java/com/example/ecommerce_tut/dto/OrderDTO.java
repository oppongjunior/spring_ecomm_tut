package com.example.ecommerce_tut.dto;

import com.example.ecommerce_tut.model.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    @NotBlank(message="Address is required")
    private String address;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItemDTO> orderItems;
}
