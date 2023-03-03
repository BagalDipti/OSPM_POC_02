package com.ospm.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ospm.product.entity.Product;
import com.ospm.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Product save(Product product) {
		// TODO Auto-generated method stub
		return this.productRepository.save(product);
	}

	@Override
	public void deleteById(int productId) {
		// TODO Auto-generated method stub
		this.productRepository.deleteById(productId);
	}

	@Override
	public Object searchProductById(int productId) {
		// TODO Auto-generated method stub
		return this.productRepository.findById(productId);
	}

	@Override
	public Object viewAllProducts() {
		// TODO Auto-generated method stub
		return this.productRepository.findAll();
	}

	@Override
	public Object searchProductByCategory(String productCategory) {
		// TODO Auto-generated method stub
		return this.productRepository.findByProductCategory(productCategory);
	}

	@Override
	public Object searchProductByName(String productName) {
		// TODO Auto-generated method stub
		return this.productRepository.findByProductName(productName);
	}

	@Override
	public Object update(Product product, int productId) {
		try {
			Product singleProduct = this.productRepository.getOne(productId);
			if(product.getProductName()!=null) {
				singleProduct.setProductName(product.getProductName());
			}
			if(product.getProductCategory()!=null) {
				singleProduct.setProductCategory(product.getProductCategory());
			}
			if(product.getProductExpDate()!=null) {
				singleProduct.setProductExpDate(product.getProductExpDate());
			}
			if(product.getProductMgfDate()!=null) {
				singleProduct.setProductMgfDate(product.getProductMgfDate());
			}
			if(product.getProductDescription()!=null) {
				singleProduct.setProductDescription(product.getProductDescription());
			}
			if(product.getProductPrice()!=0) {
				singleProduct.setProductPrice(product.getProductPrice());
			}
			if(singleProduct.getProductMgfDate().compareTo(singleProduct.getProductExpDate())<0) {
				return ResponseEntity.ok(this.productRepository.save(singleProduct)).getBody();
			}else {
				return ResponseEntity.badRequest().body("Invalid Mgf/Exp Date").getBody();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid JSON").getBody();
		}	
	}
}
