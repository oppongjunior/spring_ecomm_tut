package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.dto.ProductDTO;
import com.example.ecommerce_tut.dto.ProductListDTO;
import com.example.ecommerce_tut.exception.ResourceNotFoundException;
import com.example.ecommerce_tut.mapper.ProductMapper;
import com.example.ecommerce_tut.model.Product;
import com.example.ecommerce_tut.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final String UPLOAD_DIR = "src/main/resources/static/";

    @Transactional
    public ProductDTO saveProduct(ProductDTO productDTO, MultipartFile image) throws IOException {
        Product product = productMapper.toProduct(productDTO);
        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image);
            product.setImage(fileName);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        if (image != null && !image.isEmpty()) {
            deleteImage(existingProduct.getImage()); //delete old image
            String fileName = saveImage(image);
            existingProduct.setImage(fileName);
        }
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.deleteById(id);
        deleteImage(existingProduct.getImage());
    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.toDTO(product);
    }

    public Page<ProductListDTO> getAllProducts(Pageable pageable) {
        return productRepository.getAllProductsWithoutComment(pageable);
    }


    private String saveImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return fileName;
    }

    private void deleteImage(String path) {
        System.out.println(path);
    }
}
