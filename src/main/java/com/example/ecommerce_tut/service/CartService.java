package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.dto.CartDTO;
import com.example.ecommerce_tut.mapper.CartMapper;
import com.example.ecommerce_tut.model.Cart;
import com.example.ecommerce_tut.model.CartItem;
import com.example.ecommerce_tut.model.Product;
import com.example.ecommerce_tut.model.User;
import com.example.ecommerce_tut.repository.CartRepository;
import com.example.ecommerce_tut.repository.ProductRepository;
import com.example.ecommerce_tut.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartDTO addCart(Long productId, Long userId, Integer quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product Not Found"));

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(null, user, new ArrayList<>()));
        Optional<CartItem> cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();

        if (cartItem.isPresent()) {
            Integer oldQuantity = cartItem.get().getQuantity();
            cartItem.get().setQuantity(oldQuantity + quantity);
        } else {
            cart.getItems().add(new CartItem(null, cart, product, quantity));
        }
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDTO(savedCart);
    }

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Resource Not Found"));
        return cartMapper.toDTO(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Resource Not Found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
