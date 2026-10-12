package com.klu.ecommerce_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klu.ecommerce_backend.model.Product;
import com.klu.ecommerce_backend.service.ProductService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        Product savedProduct = productService.addProduct(product);

        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping
    public List<Product> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long productId) {

        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.updateProduct(productId, product)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }
}