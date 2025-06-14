package com.aslan.taxcalculator.service;

import com.aslan.taxcalculator.model.Product;
import com.aslan.taxcalculator.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private static final double TAX_RATE = 0.18;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    // Buraya ekledim
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getProductsByOwner(String owner) {
        return repository.findByOwner(owner);
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public Optional<Product> updateProduct(Long id, Product updated, String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }

        return repository.findById(id)
                .filter(p -> username.equals(p.getOwner()))
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setPrice(updated.getPrice());
                    return repository.save(existing);
                });
    }

    public boolean deleteProduct(Long id, String username) {
        if (username == null || username.isBlank()) {
            return false;
        }

        return repository.findById(id)
                .filter(p -> username.equals(p.getOwner()))
                .map(product -> {
                    repository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    public Optional<Double> calculateTax(Long id) {
        return repository.findById(id)
                .map(product -> product.getPrice() * TAX_RATE);
    }
}
