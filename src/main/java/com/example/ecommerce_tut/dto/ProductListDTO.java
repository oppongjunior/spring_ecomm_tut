package com.example.ecommerce_tut.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductListDTO {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String name;
    private String description;
    @Positive(message = "Price cannot be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "Quantity must not be negative")
    private Integer quantity;
    private String image;
}
