package com.example.ecommerce_tut.controller;

import com.example.ecommerce_tut.dto.ProductDTO;
import com.example.ecommerce_tut.dto.ProductListDTO;
import com.example.ecommerce_tut.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestPart("product") ProductDTO product,
                                                 @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        return ResponseEntity.ok(productService.saveProduct(product, image));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id,
                                                    @Valid @RequestPart("product") ProductDTO productDTO,
                                                    @RequestPart("image") MultipartFile image
    ) throws IOException {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO, image));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.productService.getProduct(id));
    }
}
