package com.example.ecommerce_tut.repository;

import com.example.ecommerce_tut.model.Cart;
import com.example.ecommerce_tut.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Cart> findByUserId(Long userId);
}
