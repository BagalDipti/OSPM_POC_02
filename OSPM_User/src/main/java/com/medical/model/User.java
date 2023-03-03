package com.medical.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Set;

import javax.persistence.*;


@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int userId;
	@Column(unique = true)
	private String userName;
	private String userEmail;
	private String password;
	private long userMobileNo;
	@Column(unique = true)
	private long shopId;
	private String shopName;
	private String shopAddress;
	private String role;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinTable(name = "USER_ROLES",
           joinColumns = {
           @JoinColumn(name = "USER_ID")
           },
           inverseJoinColumns = {
           @JoinColumn(name = "ROLE_ID") })
   private Set<Role> roles;

	
	public User() {
	}


	public User(int userId, String userName, String userEmail, String password, long userMobileNo, long shopId,
			String shopName, String shopAddress, String role) {
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.password = password;
		this.userMobileNo = userMobileNo;
		this.shopId = shopId;
		this.shopName = shopName;
		this.shopAddress = shopAddress;
		this.role = role;
	}

	


	@Override
	public String toString() {
		return "User [password=" + password + ", role=" + role + ", shopAddress=" + shopAddress + ", shopId=" + shopId
				+ ", shopName=" + shopName + ", userEmail=" + userEmail + ", userId=" + userId + ", userMobileNo="
				+ userMobileNo + ", userName=" + userName + "]";
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public long getUserMobileNo() {
		return userMobileNo;
	}


	public void setUserMobileNo(long userMobileNo) {
		this.userMobileNo = userMobileNo;
	}


	public long getShopId() {
		return shopId;
	}


	public void setShopId(long shopId) {
		this.shopId = shopId;
	}


	public String getShopName() {
		return shopName;
	}


	public void setShopName(String shopName) {
		this.shopName = shopName;
	}


	public String getShopAddress() {
		return shopAddress;
	}


	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}





	
}	
	
	
