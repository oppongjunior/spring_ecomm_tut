package com.example.ecommerce_tut.repository;

import com.example.ecommerce_tut.dto.ProductListDTO;
import com.example.ecommerce_tut.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.example.ecommerce_tut.dto.ProductListDTO(p.id, p.name, p.description, p.price, p.quantity, p.image) FROM Product p")
    Page<ProductListDTO> getAllProductsWithoutComment(Pageable pageable);
}
