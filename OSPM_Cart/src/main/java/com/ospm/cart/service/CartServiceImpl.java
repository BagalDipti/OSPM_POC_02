package com.ospm.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ospm.cart.entity.Cart;
import com.ospm.cart.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepository;

	@Override
	public Object save(Cart cart) {
		// TODO Auto-generated method stub
		return this.cartRepository.save(cart);
	}

	@Override
	public Object viewCartByUser(int userId) {
		// TODO Auto-generated method stub
		return this.cartRepository.findByUserId(userId);
	}

	@Override
	public void removeProduct(int cartId) {
		// TODO Auto-generated method stub
		this.cartRepository.deleteById(cartId);;
	}

	@Override
	public Cart viewCartByCartId(int cartId) {
		// TODO Auto-generated method stub
		return this.cartRepository.getById(cartId);
	}

	@Override
	public Object checkProductId(int productId) {
		// TODO Auto-generated method stub
		return this.cartRepository.checkProductId(productId);
	}

	@Override
	public Object checkUserId(int userId) {
		// TODO Auto-generated method stub
		return this.cartRepository.checkUserId(userId);
	}

	@Override
	public Cart checkCartProductOfUser(int userId, int productId) {
		// TODO Auto-generated method stub
		return (Cart) this.cartRepository.checkCartProductOfUser(userId,productId);
	}

	@Override
	public int getProductPrice(int productId) {
		// TODO Auto-generated method stub
		return this.cartRepository.getProductPrice(productId);
	}

	@Override
	public List<Integer> getAllCartId(int userId) {

		List<Integer> cartList = this.cartRepository.getAllCartId(userId);
		
		return cartList;
	}
}
