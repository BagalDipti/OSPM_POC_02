package com.ospm.product.service;

import org.springframework.stereotype.Service;

import com.ospm.product.entity.Product;

public interface ProductService {

	Product save(Product product);

	void deleteById(int productId);

	Object searchProductById(int productId);

	Object viewAllProducts();

	Object searchProductByCategory(String productCategory);

	Object searchProductByName(String productName);

	Object update(Product product, int productId);
	
}
