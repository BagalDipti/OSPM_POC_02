package com.ospm.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ospm.cart.entity.Cart;
import com.ospm.cart.service.CartService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/cart")
public class CartController {

	private final static Logger LOGGER = LoggerFactory.getLogger(CartController.class);

	@Autowired
	private CartService cartService;
	@Autowired
	private RestTemplate restTemplate;

	// test cart controller
	@GetMapping("/test")
	public String testCart() {
		return "cart service works";
	}

	// add to cart by user id
	@PostMapping("/addToCartByUserId/{userId}")
	public ResponseEntity<?> addToCartByUserId(@RequestBody Cart cart, @PathVariable("userId") int userId) {
		try {
			LOGGER.info("POST /cart/addToCartByUserId/" + userId);

			System.out.println("//////////////////////////////\n");
			System.out.println(userId);
			System.out.println("///////////////////////////////////\n");
			System.out.println(cart);
			System.out.println("///////////////////////////////////\n");
			
			int productId = cart.getProductId();
			if (this.cartService.checkProductId(productId) != null && this.cartService.checkUserId(userId) != null) {
				Cart singleCart = this.cartService.checkCartProductOfUser(userId, productId);
				if (singleCart != null) {
					singleCart.setQuantity(cart.getQuantity());
					singleCart.setProductTotalPrice(cart.getQuantity() * this.cartService.getProductPrice(productId));
					return ResponseEntity.ok(this.cartService.save(singleCart));
				} else {
					cart.setUserId(userId);
					cart.setProductTotalPrice(cart.getQuantity() * this.cartService.getProductPrice(productId));
					return ResponseEntity.ok(this.cartService.save(cart));
				}
			} else {
				return ResponseEntity.badRequest().body("Invalid Input");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid JSON");
		}
	}

	// view cart of a user
	@GetMapping("/viewCartByUser/{userId}")
	public ResponseEntity<?> viewCartByUser(@PathVariable("userId") int userId) {
		if (this.cartService.checkUserId(userId) != null) {
			LOGGER.info("GET /cart/viewCartByUser/" + userId);

			return ResponseEntity.ok(this.cartService.viewCartByUser(userId));
		}
		return new ResponseEntity<>("Error occurred while fetching cart data.", HttpStatus.NOT_FOUND);
	}

	// remove a product by userId and cartid
	@DeleteMapping("/removeProduct/{userId}/{cartId}")
	public ResponseEntity<?> removeProduct(@PathVariable("userId") int userId, @PathVariable("cartId") int cartId) {
		try {
			LOGGER.info("DELETE /cart/removeProduct/" + userId + "/" + cartId);

			Cart cart = this.cartService.viewCartByCartId(cartId);
			if (cart.getUserId() == userId) {
				this.cartService.removeProduct(cartId);
				return ResponseEntity.ok("Cart deleted successfully");
			} else {
				return new ResponseEntity<>("Cart not found.", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching cart data.", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getAllCartId/{userId}")
	public List<Integer> getAllCartId(@PathVariable("userId") int userId) {
		LOGGER.info("DELETE /cart/removeProduct/" + userId);
		System.out.println("Inside Cart");

		List<Integer> cartList = this.cartService.getAllCartId(userId);
		return cartList;

	}

}
