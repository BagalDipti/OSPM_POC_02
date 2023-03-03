package com.providerService.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Orderr {
	private int orderId;
	private LocalDate orderDate = LocalDate.now();
	private String orderStatus;
	private int productId;
	private int quantity;
	private int productTotalPrice;
	private int userId;
	private String paymentStatus;
	private String paymentMode;
	
	
	public Orderr() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Orderr(int orderId, LocalDate orderDate, String orderStatus, int productId, int quantity,
			int productTotalPrice, int userId, String paymentStatus, String paymentMode) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.productId = productId;
		this.quantity = quantity;
		this.productTotalPrice = productTotalPrice;
		this.userId = userId;
		this.paymentStatus = paymentStatus;
		this.paymentMode = paymentMode;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	@Override
	public String toString() {
		return "Orderr [orderId=" + orderId + ", orderDate=" + orderDate + ", orderStatus=" + orderStatus
				+ ", productId=" + productId + ", quantity=" + quantity + ", productTotalPrice=" + productTotalPrice
				+ ", userId=" + userId + ", paymentStatus=" + paymentStatus + ", paymentMode=" + paymentMode + "]";
	}
	
}
