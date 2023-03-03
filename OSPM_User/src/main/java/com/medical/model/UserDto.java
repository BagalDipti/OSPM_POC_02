package com.medical.model;
public class UserDto {


	private String userName;
	private String userEmail;
	private String password;
	private long userMobileNo;
	private long shopId;
	private String shopName;
	private String shopAddress;
	private String role;
    

  

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




    public User getUserFromDto(){
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setUserEmail(userEmail);
        user.setUserMobileNo(userMobileNo);
        user.setShopId(shopId);
        user.setShopName(shopName);
        user.setShopAddress(shopAddress);
        user.setRole(role);
        
        return user;
    }


}
