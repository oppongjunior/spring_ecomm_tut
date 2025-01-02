package com.example.ecommerce_tut.mapper;

import com.example.ecommerce_tut.dto.CartDTO;
import com.example.ecommerce_tut.dto.CartItemDTO;
import com.example.ecommerce_tut.model.Cart;
import com.example.ecommerce_tut.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    CartDTO toDTO(Cart cart);

    @Mapping(target = "user.id", source = "userId")
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target = "productId", source = "product.id")
    CartItemDTO toDTO (CartItem cartItem);

    @Mapping(target = "product.id", source="productId")
    CartItem toEntity(CartItemDTO cartItemDTO);
}
