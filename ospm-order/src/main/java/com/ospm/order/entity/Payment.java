package com.ospm.order.entity;

public class Payment {

	private int orderid;
	private double amount;
	private int custid;
	private String txnToken;
	private String paymentMode;
	private long cardNumber;
	private int cvv;
	private long expDate;
	
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Payment(int orderid, double amount, int custid, String txnToken, String paymentMode, long cardNumber,
			int cvv, long expDate) {
		super();
		this.orderid = orderid;
		this.amount = amount;
		this.custid = custid;
		this.txnToken = txnToken;
		this.paymentMode = paymentMode;
		this.cardNumber = cardNumber;
		this.cvv = cvv;
		this.expDate = expDate;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getCustid() {
		return custid;
	}
	public void setCustid(int custid) {
		this.custid = custid;
	}
	public String getTxnToken() {
		return txnToken;
	}
	public void setTxnToken(String txnToken) {
		this.txnToken = txnToken;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public long getExpDate() {
		return expDate;
	}
	public void setExpDate(long expDate) {
		this.expDate = expDate;
	}
	@Override
	public String toString() {
		return "Payment [orderid=" + orderid + ", amount=" + amount + ", custid=" + custid + ", txnToken=" + txnToken
				+ ", paymentMode=" + paymentMode + ", cardNumber=" + cardNumber + ", cvv=" + cvv + ", expDate="
				+ expDate + "]";
	}

	
	




}