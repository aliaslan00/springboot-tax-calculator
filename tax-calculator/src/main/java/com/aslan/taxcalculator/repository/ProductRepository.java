package com.aslan.taxcalculator.repository;

import com.aslan.taxcalculator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwner(String owner);
}
