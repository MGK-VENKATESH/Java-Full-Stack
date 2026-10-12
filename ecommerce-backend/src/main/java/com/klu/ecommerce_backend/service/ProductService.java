package com.klu.ecommerce_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.klu.ecommerce_backend.model.Product;
import com.klu.ecommerce_backend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Cacheable("products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Long productId, Product updatedProduct) {

        Product existingProduct = productRepository
                .findById(productId)
                .orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setRating(updatedProduct.getRating());
        existingProduct.setStatus(updatedProduct.getStatus());

        return productRepository.save(existingProduct);
    }


    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long productId) {

        productRepository.deleteById(productId);
    }
}