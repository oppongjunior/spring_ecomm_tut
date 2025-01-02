package com.example.ecommerce_tut.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String name;
    private String description;
    @Positive(message = "Price cannot be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "Quantity must not be negative")
    private Integer quantity;
    private List<CommentDTO> comments;
    private String image;
}
