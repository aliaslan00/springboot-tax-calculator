package com.aslan.taxcalculator.controller;

import com.aslan.taxcalculator.model.Product;
import com.aslan.taxcalculator.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    private static final String HEADER_USERNAME = "X-USERNAME";

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestHeader(HEADER_USERNAME) String username, @RequestBody Product product) {
        product.setOwner(username);
        return new ResponseEntity<>(service.createProduct(product), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader(HEADER_USERNAME) String username, @PathVariable Long id, @RequestBody Product product) {
        Optional<Product> updated = service.updateProduct(id, product, username);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update others' products");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader(HEADER_USERNAME) String username, @PathVariable Long id) {
        boolean deleted = service.deleteProduct(id, username);
        if (deleted) {
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete others' products");
    }

    @GetMapping("/{id}/tax")
    public ResponseEntity<?> tax(@PathVariable Long id) {
        Optional<Double> taxOpt = service.calculateTax(id);
        if (taxOpt.isPresent() && taxOpt.get() > 0) {
            return ResponseEntity.ok(taxOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }
}
