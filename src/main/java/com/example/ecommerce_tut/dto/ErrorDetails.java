package com.example.ecommerce_tut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String details;
    private LocalDateTime timestamp;
}
