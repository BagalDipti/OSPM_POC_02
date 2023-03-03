package com.ospm.cart.service;

import java.util.List;

import com.ospm.cart.entity.Cart;

public interface CartService {

	Object save(Cart cart);

	Object viewCartByUser(int userId);

	void removeProduct(int cartId);

	Cart viewCartByCartId(int cartId);

	Object checkProductId(int productId);

	Object checkUserId(int userId);

	Cart checkCartProductOfUser(int userId, int productId);

	int getProductPrice(int productId);
	
	List<Integer> getAllCartId(int userId);

}
