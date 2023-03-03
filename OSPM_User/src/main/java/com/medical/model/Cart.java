package com.medical.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Cart {
	private int cartId;
	private int productId;
	private int userId;
	private LocalDate dateAdded = LocalDate.now();
	private int quantity;
	private int productTotalPrice;
	public Cart() {
		super();
	}
	public Cart(int cartId, int productId, int userId, LocalDate dateAdded, int quantity, int productTotalPrice) {
		super();
		this.cartId = cartId;
		this.productId = productId;
		this.userId = userId;
		this.dateAdded = dateAdded;
		this.quantity = quantity;
		this.productTotalPrice = productTotalPrice;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public LocalDate getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getProductTotalPrice() {
		return productTotalPrice;
	}
	public void setProductTotalPrice(int productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
	@Override
	public String toString() {
		return "Cart [cartId=" + cartId + ", productId=" + productId + ", userId=" + userId + ", dateAdded=" + dateAdded
				+ ", quantity=" + quantity + ", productTotalPrice=" + productTotalPrice + "]";
	}
	
}
