package com.example.ecommerce_tut.mapper;

import com.example.ecommerce_tut.dto.OrderDTO;
import com.example.ecommerce_tut.dto.OrderItemDTO;
import com.example.ecommerce_tut.model.Order;
import com.example.ecommerce_tut.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items")
    OrderDTO toDto(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOs(List<Order> orders);

    List<Order> toEntities(List<OrderDTO> orderDTOs);

    @Mapping(target = "productId", source="product.id")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    @Mapping(target="product.id", source="productId")
    OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO);
}
