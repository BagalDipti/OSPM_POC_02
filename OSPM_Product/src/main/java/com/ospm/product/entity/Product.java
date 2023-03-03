package com.ospm.product.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int productId;
	private String productName;
	private String productCategory;
	private LocalDate productMgfDate = LocalDate.now();
	private LocalDate productExpDate;
	private int productPrice;
	private String productDescription;
	public Product() {
		super();
	}
	public Product(int productId, String productName, String productCategory, LocalDate productMgfDate,
			LocalDate productExpDate, int productPrice, String productDescription) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productCategory = productCategory;
		this.productMgfDate = productMgfDate;
		this.productExpDate = productExpDate;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public LocalDate getProductMgfDate() {
		return productMgfDate;
	}
	public void setProductMgfDate(LocalDate productMgfDate) {
		this.productMgfDate = productMgfDate;
	}
	public LocalDate getProductExpDate() {
		return productExpDate;
	}
	public void setProductExpDate(LocalDate productExpDate) {
		this.productExpDate = productExpDate;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productCategory="
				+ productCategory + ", productMgfDate=" + productMgfDate + ", productExpDate=" + productExpDate
				+ ", productPrice=" + productPrice + ", productDescription=" + productDescription + "]";
	}
	
	
	
}
