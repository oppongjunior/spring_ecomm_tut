package com.example.ecommerce_tut.mapper;

import com.example.ecommerce_tut.dto.ProductDTO;
import com.example.ecommerce_tut.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toProduct(ProductDTO productDTO);
}
