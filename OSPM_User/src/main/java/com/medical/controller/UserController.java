package com.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.medical.config.TokenProvider;
import com.medical.model.AuthToken;
import com.medical.model.Cart;
import com.medical.model.LoginUser;
import com.medical.model.Payment;
import com.medical.model.Product;
import com.medical.model.User;
import com.medical.model.UserDto;
import com.medical.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/user")
public class UserController {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider jwtTokenUtil;

	// To register User
	@PostMapping("/signup")
	public Object saveUser(@RequestBody UserDto user) {
		try {
			LOGGER.info("POST /user/signup/" + "USER signed up.");

			return userService.save(user);
		} catch (Exception e) {
			return new ResponseEntity<>("Shop-Id is already present", HttpStatus.CONFLICT);
		}
	}


	// To generate token for user

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(authentication);
		return ResponseEntity.ok(new AuthToken(token));

	}

	// Find User By userId
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/findUserById/{userId}")
	public ResponseEntity<?> findUser(@PathVariable("userId") Integer userId) {
		try {
			LOGGER.info("GET /user/findUserById/" + userId);

			return ResponseEntity.ok(userService.findById(userId));
		} catch (Exception e) {
			return new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);
		}
	}

	// To update the user by ID
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/updateUserById/{userId}")
	public Object updateUser(@RequestBody User user, @PathVariable("userId") Integer userId) {
		try {
			if (user.getShopId() != 0) {
				LOGGER.info("PUT /user/updateUserById/" + userId);
				return ResponseEntity.ok(this.userService.userUpdate(user, userId));

			} else {
				LOGGER.info("PUT  Bad Request :/user/updateUserById/" + userId);

				return ResponseEntity.badRequest().body("Invalid Input");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}

	// To view all User
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/getAllUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Object> getAllUser() {
		LOGGER.info("GET /user/getAllUser");

		return (List<Object>) this.userService.getAllUsers();
	}

	// To get user by ShopId
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/getUserByShopId/{shopId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserByShopId(@PathVariable("shopId") long shopId) {
		try {
			LOGGER.info("GET /user/getUserByShopId/" + shopId);

			if (this.userService.getUserByShopId(shopId) != null)
				return ResponseEntity.ok(this.userService.getUserByShopId(shopId));
			else
				return new ResponseEntity<>("Please Enter valid Shop-Id", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Please Enter valid Details", HttpStatus.BAD_REQUEST);
		}
	}

	// Delete User By Id
	@PreAuthorize("hasRole('USER')")
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<?> deleteUserById(@PathVariable("userId") int userId) {
		try {
			LOGGER.info("DELETE /user/deleteUser/" + userId);

			this.userService.deleteById(userId);
			return ResponseEntity.ok("User deleted successfully");
		} catch (Exception e) {
			return new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);
		}
	}

	// Product Related Methods
	// To view all Products
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/viewAllProducts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllProduct() {
		try {

			LOGGER.info("GET /user/viewAllProducts/" + "ok");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Product> entity = new HttpEntity<Product>(headers);
			RestTemplate rt = new RestTemplate();

			ResponseEntity<Product[]> responseEntity = rt.exchange("http://192.168.49.2:30167/product/viewAllProducts",
					HttpMethod.GET, entity, Product[].class);

//			@SuppressWarnings("unchecked")
//			List<Object> users = (List<Object>) this.restTemplate
//					.getForObject("http://ospm-product/product/viewAllProducts", Object.class);
			return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching products data. ", HttpStatus.NOT_FOUND);
		}
	}

	// To view product by Id
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/searchProductById/{productId}")
	public ResponseEntity<?> searchProductById(@PathVariable("productId") long productId) {
		try {

			LOGGER.info("GET /user/searchProductById/" + productId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String product = rt.exchange("http://192.168.49.2:30167/product/searchProductById/" + productId,
					HttpMethod.GET, entity, String.class).getBody();

//			Object product = this.restTemplate
//					.getForObject("http://ospm-product/product/searchProductById/" + productId, Object.class);
			if (product != null)
				return new ResponseEntity<>(product, HttpStatus.OK);
			else
				return new ResponseEntity<>("Please Enter valid Product-Id", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Please Enter valid Product-Id", HttpStatus.NOT_FOUND);
		}
	}

	// search product by name
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/searchProductByName/{productName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchProductByName(@PathVariable("productName") String productName) {
		try {
			LOGGER.info("GET /user/searchProductByName/" + productName);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String product = rt.exchange("http://192.168.49.2:30167/product/searchProductByName/" + productName,
					HttpMethod.GET, entity, String.class).getBody();

//			@SuppressWarnings("unchecked")
//			List<Object> products = (List<Object>) this.restTemplate
//					.getForObject("http://ospm-product/product/searchProductByName/" + productName, Object.class);
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching products data. ", HttpStatus.NOT_FOUND);
		}
	}

	// search product by category
	@GetMapping(value = "/searchProductByCategory/{productCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchProductByCategory(@PathVariable("productCategory") String productCategory) {
		try {
			LOGGER.info("GET /user/searchProductByCategory/" + productCategory);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String product = rt.exchange("http://192.168.49.2:30167/product/searchProductByCategory/" + productCategory,
					HttpMethod.GET, entity, String.class).getBody();

//			@SuppressWarnings("unchecked")
//			List<Object> products = (List<Object>) this.restTemplate.getForObject(
//					"http://ospm-product/product/searchProductByCategory/" + productCategory, Object.class);
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching products data. ", HttpStatus.NOT_FOUND);
		}
	}

	// Cart related services------------------------------------>>>
	// add to cart by user id and update the cart
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/addToCartByUserId/{userId}")
	public ResponseEntity<?> addToCartByUserId(@RequestBody Cart cart, @PathVariable("userId") int userId) {
		try {
			LOGGER.info("POST /user/addToCartByUserId/" + userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Cart> entity = new HttpEntity<Cart>(cart, headers);
			RestTemplate rt = new RestTemplate();

			ResponseEntity<Cart> responseEntity = rt.exchange(
					"http://192.168.49.2:30168/cart/addToCartByUserId/" + userId, HttpMethod.POST, entity, Cart.class);

			return ResponseEntity.ok(responseEntity.getBody());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input 2");
		}
	}

	// view cart of a user
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/viewCartByUser/{userId}")
	public ResponseEntity<?> viewCartByUser(@PathVariable("userId") int userId) {
		try {
			LOGGER.info("GET /user/viewCartByUser/" + userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String carts = rt.exchange("http://192.168.49.2:30168/cart/viewCartByUser/" + userId, HttpMethod.GET,
					entity, String.class).getBody();

//			@SuppressWarnings("unchecked")
//			List<Object> carts = (List<Object>) this.restTemplate
//					.getForObject("http://ospm-cart/cart/viewCartByUser/" + userId, Object.class);
			return new ResponseEntity<>(carts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching Cart data.", HttpStatus.NOT_FOUND);
		}
	}

	// remove a product by userid and cartId
	@DeleteMapping("/removeProduct/{userId}/{cartId}")
	public ResponseEntity<?> removeProduct(@PathVariable("userId") int userId, @PathVariable("cartId") int cartId) {
		try {
			LOGGER.info("DELETE /user/removeProduct/" + userId + "/" + cartId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			rt.exchange("http://192.168.49.2:30168/cart/removeProduct/" + userId + "/" + cartId, HttpMethod.DELETE,
					entity, String.class).getBody();

//			this.restTemplate.delete("http://ospm-cart/cart/removeProduct/" + userId + "/" + cartId, Object.class);
			return new ResponseEntity<>("Cart deleted succeesfully", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while deleting cart.", HttpStatus.NOT_FOUND);
		}
	}

	// order related operation
	// place order by userId ----> (need to work on it, not getting as expected)
	/*
	 * @PreAuthorize("hasRole('USER')")
	 * 
	 * @PostMapping(value = "/placeOrder/{userId}") public ResponseEntity<?>
	 * placeOrder(@PathVariable("userId") int userId) { try {
	 * LOGGER.info("POST /user/placeOrder/" + userId);
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setContentType(MediaType.APPLICATION_JSON); HttpEntity<String> entity
	 * = new HttpEntity<String>(headers); RestTemplate rt = new RestTemplate();
	 * 
	 * String order = rt.exchange("http://192.168.49.2:30170/order/placeOrder/" +
	 * userId, HttpMethod.POST, entity, String.class).getBody();
	 * 
	 * // String order = (String)
	 * this.restTemplate.postForObject("http://ospm-order/order/placeOrder/" +
	 * userId, // null, String.class); return ResponseEntity.ok(order); } catch
	 * (Exception e) { return ResponseEntity.badRequest().body(e.getMessage() +
	 * "   Invalid"); } }
	 */    
    
 // order related operation
 	// place order by userId ----> (need to work on it, not getting as expected)
 	@PreAuthorize("hasRole('USER')")
 	@PostMapping(value = "/placeOrder/{userId}")
 	public ResponseEntity<?> placeOrder(@PathVariable("userId") int userId, @RequestBody Payment payment) {
 		try {

 			LOGGER.info("POST /user/placeOrder/" + userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Payment> entity = new HttpEntity<Payment>(payment,headers);
			RestTemplate rt = new RestTemplate();

			ResponseEntity responseEntity = rt.exchange("http://192.168.49.2:30170/order/placeOrder/" + userId, HttpMethod.POST, entity,
					ResponseEntity.class).getBody();

 			
 			return ResponseEntity.ok(responseEntity);
 		} catch (Exception e) {
 			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
 		}
 	}
    
    

	// view order of a user
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/viewOrderByUser/{userId}")
	public ResponseEntity<?> viewOrderByUser(@PathVariable("userId") int userId) {
		try {
			LOGGER.info("GET /user/viewOrderByUser/" + userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String order = rt.exchange("http://192.168.49.2:30170/order/viewOrderByUser/" + userId, HttpMethod.GET,
					entity, String.class).getBody();

//			@SuppressWarnings("unchecked")
//			List<Object> order = (List<Object>) this.restTemplate
//					.getForObject("http://ospm-order/order/viewOrderByUser/" + userId, Object.class);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}
}
