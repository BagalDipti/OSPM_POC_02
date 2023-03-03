package com.ospm.order.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Orderr {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int orderId;
	private LocalDate orderDate = LocalDate.now();
	private String orderStatus;
	private double totalAmount;
	private int userId;
	private String paymentStatus;
	private String paymentMode;
	
	public Orderr() {
	}

	public Orderr(int orderId, LocalDate orderDate, String orderStatus, double totalAmount, int userId,
			String paymentStatus, String paymentMode) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.totalAmount = totalAmount;
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

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
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
				+ ", totalAmount=" + totalAmount + ", userId=" + userId + ", paymentStatus=" + paymentStatus
				+ ", paymentMode=" + paymentMode + "]";
	}
		
}
