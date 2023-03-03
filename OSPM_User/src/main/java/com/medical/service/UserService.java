package com.medical.service;

import java.util.List;

import com.medical.model.User;
import com.medical.model.UserDto;

public interface UserService {
	User save(User user);

	Object userUpdate(User user, Integer userId);
	
	User findById(int id);

	Object getAllUsers();

	void deleteById(int userId);

	Object getUserByShopId(long shopId);

	User save(UserDto user);

}
