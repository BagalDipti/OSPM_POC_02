package com.providerService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Provider {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	private String providerName;
	@Column(unique = true)
	private String userName;
	
	private String password;
	
	private String email;
	private String role;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinTable(name = "PROVIDER_ROLES",
           joinColumns = {
           @JoinColumn(name = "USER_ID")
           },
           inverseJoinColumns = {
           @JoinColumn(name = "ROLE_ID") })
   private Set<Role> roles;

	
	public Provider() {
	}


	public Provider(Long id, String providerName, String userName, String password, String email, String role) {
		this.id = id;
		this.providerName = providerName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.role = role;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getProviderName() {
		return providerName;
	}


	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
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


	@Override
	public String toString() {
		return "Provider [email=" + email + ", id=" + id + ", password=" + password + ", providerName=" + providerName
				+ ", role=" + role + ", roles=" + roles + ", userName=" + userName + "]";
	}
	
	
}
