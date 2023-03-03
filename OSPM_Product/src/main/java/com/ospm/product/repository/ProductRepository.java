package com.ospm.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ospm.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByProductCategory(String productCategory);

	List<Product> findByProductName(String productName);

}
