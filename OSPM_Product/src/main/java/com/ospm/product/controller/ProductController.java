package com.ospm.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ospm.product.entity.Product;
import com.ospm.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/product")
public class ProductController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	// testing controller
	@GetMapping("/test")
	public String testProductController() {
		LOGGER.info("GET /test/" + "ok");

		return "test success";
	}

	// add a product to database
	@PostMapping("/addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		try {
			LOGGER.info("POST /product/addProduct");

			if (product.getProductMgfDate().compareTo(product.getProductExpDate()) < 0) {
				return ResponseEntity.ok(this.productService.save(product));
			} else {
				return ResponseEntity.badRequest().body("Invalid Mgf/Exp Date");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid JSON");
		}
	}

	// delete product by productId
	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<?> deleteProductById(@PathVariable("productId") int productId) {
		try {
			LOGGER.info("DELETE /product/deleteProduct/" + productId);

			this.productService.deleteById(productId);
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			return ResponseEntity.ok(false);
		}
	}

	// search product by productId

	@GetMapping("/searchProductById/{productId}")
	public ResponseEntity<?> searchProductById(@PathVariable("productId") int productId) {
		LOGGER.info("GET /product/searchProductById/" + productId);

		return ResponseEntity.ok(this.productService.searchProductById(productId));
	}

	// search product by name
	@SuppressWarnings("unchecked")
	@GetMapping("/searchProductByName/{productName}")
	public List<Object> searchProductByName(@PathVariable("productName") String productName) {
		LOGGER.info("GET /product/searchProductByName/" + productName);

		return (List<Object>) this.productService.searchProductByName(productName);
	}

	// search product by category
	@SuppressWarnings("unchecked")
	@GetMapping("/searchProductByCategory/{productCategory}")
	public List<Object> searchProductByCategory(@PathVariable("productCategory") String productCategory) {
		LOGGER.info("GET /product/searchProductByCategory/" + productCategory);

		return (List<Object>) this.productService.searchProductByCategory(productCategory);
	}

	// get all product
	@SuppressWarnings("unchecked")
	@GetMapping("/viewAllProducts")
	public List<Object> viewAllProducts() {
		LOGGER.info("GET /product/viewAllProducts");

		return (List<Object>) this.productService.viewAllProducts();
	}

	// update product by productId
	@PutMapping("/updateByProductId/{productId}")
	public ResponseEntity<?> updateByProductId(@RequestBody Product product, @PathVariable("productId") int productId) {
		try {
			LOGGER.info("UPDATE /product/updateByProductId/" + productId);
			
			return ResponseEntity.ok(this.productService.update(product, productId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}
}
