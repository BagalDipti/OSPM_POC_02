package com.providerService.controller;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.providerService.config.TokenProvider;
import com.providerService.model.AuthToken;
import com.providerService.model.LoginUser;
import com.providerService.model.Product;
import com.providerService.model.Provider;
import com.providerService.service.ProviderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin("*")
@RequestMapping("/provider")
public class ProviderController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProviderController.class);

	@Autowired
	private ProviderService providerService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
    private TokenProvider jwtTokenUtil;

	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "This is Test Method";
	}


	// To register User
	@PostMapping("/sign")
	public Object saveUser(@RequestBody Provider provider) {
	   try {
		   return providerService.add(provider);
	   } catch (Exception e) {
		   return new ResponseEntity<>("username present", HttpStatus.CONFLICT);
	   }
   }
	   // To generate token for user

   @RequestMapping(value = "/auth", method = RequestMethod.POST)
   public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
	   
		   final Authentication authentication = authenticationManager.authenticate(
			   new UsernamePasswordAuthenticationToken(
					   loginUser.getUserName(),
					   loginUser.getPassword()
			   )
	   );
	   SecurityContextHolder.getContext().setAuthentication(authentication);
	   final String token = jwtTokenUtil.generateToken(authentication);
	   return ResponseEntity.ok(new AuthToken(token));
	   
   }

    @PreAuthorize("hasRole('PROVIDER')")
	@GetMapping("/showAllProviders")
	public ResponseEntity<?> getAllProviders() {
		try {
			LOGGER.info("GET /provider/showAllProviders");

			return new ResponseEntity<>(providerService.getAllProviders(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching all Accounts data",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Find User By userId
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping("/findUserById/{userId}")
	public Object findUserById(@PathVariable("userId") Integer userId) {
		try {
			LOGGER.info("GET /provider/findUserById/" + userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String user = rt.exchange("http://192.168.49.2:30165/user/findUserById/" + userId, HttpMethod.GET, entity,
					String.class).getBody();

			// Object user =
			// this.restTemplate.getForObject("http://ospm-user/user/findUserById/" +
			// userId, Object.class);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching user data with user Id : " + userId,
					HttpStatus.NOT_FOUND);
		}
	}

	// To view all User
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping(value = "/getAllUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUser() {
		try {
			LOGGER.info("GET /provider/getAllUser");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String user = rt.exchange("http://192.168.49.2:30165/user/getAllUser", HttpMethod.GET, entity, String.class)
					.getBody();

			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching users data. ", HttpStatus.NOT_FOUND);
		}
	}

	// To get user by ShopId
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping(value = "/getUserByShopId/{shopId}")
	public ResponseEntity<?> getUserByShopId(@PathVariable("shopId") long shopId) {
		try {
			LOGGER.info("GET /provider/getUserByShopId/" + shopId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String user = rt.exchange("http://192.168.49.2:30165/user/getUserByShopId/" + shopId, HttpMethod.GET,
					entity, String.class).getBody();

			// Object user =
			// this.restTemplate.getForObject("http://ospm-user/user/getUserByShopId/" +
			// shopId,
			// Object.class);
			if (user != null)
				return new ResponseEntity<>(user, HttpStatus.OK);
			else
				return new ResponseEntity<>("Please Enter valid Shop-Id", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Please Enter valid Shop-Id", HttpStatus.NOT_FOUND);
		}
	}

	// Delete User By Id
	@PreAuthorize("hasRole('PROVIDER')")
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<?> deleteUserById(@PathVariable("userId") int userId) {
		try {
			LOGGER.info("DELETE /provider/deleteUser/" + userId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			rt.exchange("http://192.168.49.2:30165/user/deleteUser/" + userId, HttpMethod.DELETE, entity, String.class)
					.getBody();

//			this.restTemplate.delete("http://ospm-user/user/deleteUser/" + userId, Object.class);

			return new ResponseEntity<>("Record Deleted.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("User Not found", HttpStatus.NOT_FOUND);
		}
	}

	// Product Related Methods
	// To view all Products
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping(value = "/viewAllProducts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllProduct() {
		try {
			LOGGER.info("GET /provider/viewAllProducts");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String user = rt
					.exchange("http://192.168.49.2:30167/product/viewAllProducts", HttpMethod.GET, entity, String.class)
					.getBody();

//			@SuppressWarnings("unchecked")
//			List<Object> users = (List<Object>) this.restTemplate
//					.getForObject("http://ospm-product/product/viewAllProducts", Object.class);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching products data. ", HttpStatus.NOT_FOUND);
		}
	}

	// To view product by Id
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping(value = "/searchProductById/{productId}")
	public ResponseEntity<?> searchProductById(@PathVariable("productId") long productId) {
		try {
			LOGGER.info("GET /provider/searchProductById/" + productId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String product = rt.exchange("http://192.168.49.2:30167/product/searchProductById/" + productId,
					HttpMethod.GET, entity, String.class).getBody();

			// Object product = this.restTemplate
			// .getForObject("http://ospm-product/product/searchProductById/" + productId,
			// Object.class);
			if (product != null)
				return new ResponseEntity<>(product, HttpStatus.OK);
			else
				return new ResponseEntity<>("Please Enter valid Product-Id", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Please Enter valid Product-Id", HttpStatus.NOT_FOUND);
		}
	}

	// search product by name
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping(value = "/searchProductByName/{productName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchProductByName(@PathVariable("productName") String productName) {
		try {
			LOGGER.info("GET /provider/searchProductByName/" + productName);

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
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping(value = "/searchProductByCategory/{productCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchProductByCategory(@PathVariable("productCategory") String productCategory) {
		try {
			LOGGER.info("GET /provider/searchProductByCategory/" + productCategory);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String products = rt
					.exchange("http://192.168.49.2:30167/product/searchProductByCategory/" + productCategory,
							HttpMethod.GET, entity, String.class)
					.getBody();

//			@SuppressWarnings("unchecked")
//			List<Object> products = (List<Object>) this.restTemplate.getForObject(
//					"http://ospm-product/product/searchProductByCategory/" + productCategory, Object.class);
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching products data. ", HttpStatus.NOT_FOUND);
		}
	}

	// delete product by productId ---> (need to improve this controller)
	@PreAuthorize("hasRole('PROVIDER')")
	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<?> deleteProductById(@PathVariable("productId") int productId) {
		try {
			LOGGER.info("DELETE /provider/deleteProduct/" + productId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String product = rt.exchange("http://192.168.49.2:30167/product/searchProductById/" + productId,
					HttpMethod.DELETE, entity, String.class).getBody();

//			Object product = this.restTemplate
//					.getForObject("http://ospm-product/product/searchProductById/" + productId, Object.class);

			if (product != null) {

				HttpHeaders headerr = new HttpHeaders();
				headerr.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entityy = new HttpEntity<String>(headers);
				RestTemplate rtt = new RestTemplate();

				rtt.exchange("http://192.168.49.2:30167/product/deleteProduct/" + productId, HttpMethod.DELETE, entityy,
						String.class).getBody();

//				this.restTemplate.delete("http://ospm-product/product/deleteProduct/" + productId, Object.class);

				return new ResponseEntity<>("Product deleted succeesfully", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while deleting product.", HttpStatus.NOT_FOUND);
		}
	}

	// add a product to database
	@PreAuthorize("hasRole('PROVIDER')")
	@PostMapping("/addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		try {
			LOGGER.info("POST /provider/addProduct");

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<Product> entity = new HttpEntity<Product>(product, headers);
			RestTemplate rt = new RestTemplate();
			/*
			 * System.out.println("111111111111111111111111111111111111111111111\n");
			 * System.out.println(product);
			 * System.out.println("1111111111111111111111111111111111111111\n");
			 * System.out.println(entity);
			 */
			ResponseEntity<Product> responseEntity = rt.exchange("http://192.168.49.2:30167/product/addProduct",
					HttpMethod.POST, entity, Product.class);

			System.out.println("222222222222222222222222222222222222222222222222222");
			return ResponseEntity.ok(responseEntity.getBody());
//			Object product = this.restTemplate.postForObject("http://ospm-product/product/addProduct", jsonObject,
//					Object.class);
//			return ResponseEntity.ok(product);
		} catch (Exception e) {
			System.out.println(
					"----------------------------------\n" + e.getMessage() + "\n--------------------------------");
			return ResponseEntity.badRequest().body("Invalid JSON 2");
		}
	}

	// update product by productId
	@PreAuthorize("hasRole('PROVIDER')")
	@PutMapping("/updateByProductId/{productId}")
	public ResponseEntity<?> updateByProductId(@RequestBody Object jsonObject,
			@PathVariable("productId") int productId) {
		try {
			LOGGER.info("PUT /provider/updateByProductId/" + productId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Product> entity = new HttpEntity<Product>(headers);
			RestTemplate rt = new RestTemplate();

			rt.exchange("http://192.168.49.2:30167/product/updateByProductId/" + productId, HttpMethod.PUT, entity,
					String.class).getBody();

//			this.restTemplate.put("http://ospm-product/product/updateByProductId/" + productId, jsonObject);
			return ResponseEntity.ok("Product " + productId + " Updated.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input1");
		}
	}

	// order related operation
	// get all orders
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping("/allOrders")
	public ResponseEntity<?> allOrders() {
		try {
			LOGGER.info("GET /provider/allOrders");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String order = rt
					.exchange("http://192.168.49.2:30170/order/allOrders", HttpMethod.GET, entity, String.class)
					.getBody();

//			Object order = this.restTemplate.getForObject("http://ospm-order/order/allOrders", Object.class);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}

	// get order by date
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping("/getOrderByYearMonthDay/{year}/{month}/{day}")
	public ResponseEntity<?> getOrderByYearMonthDay(@PathVariable("year") String year,
			@PathVariable("month") String month, @PathVariable("day") String day) {
		try {

			/*
			 * HttpHeaders headers = new HttpHeaders();
			 * headers.setContentType(MediaType.APPLICATION_JSON); HttpEntity<String> entity
			 * = new HttpEntity<String>(headers); RestTemplate rt = new RestTemplate();
			 * 
			 * String user=rt.exchange("http://192.168.49.2:30165/user/getAllUser",
			 * HttpMethod.GET, entity, String.class).getBody();
			 */

			Object order = this.restTemplate.getForObject(
					"http://ospm-order/order/getOrderByYearMonthDay/" + year + "/" + month + "/" + day, Object.class);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}

	// get order by order status
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping("/getOrderByStatus/{orderStatus}")
	public ResponseEntity<?> getOrderByStatus(@PathVariable("orderStatus") String orderStatus) {
		try {
			LOGGER.info("GET /provider/getOrderByStatus/" + orderStatus);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String order = rt.exchange("http://192.168.49.2:30170/order/getOrderByStatus/" + orderStatus,
					HttpMethod.GET, entity, String.class).getBody();

//			Object order = this.restTemplate.getForObject("http://ospm-order/order/getOrderByStatus/" + orderStatus,
//					Object.class);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}

	// get order details by order id
	@PreAuthorize("hasRole('PROVIDER')")
	@GetMapping("/viewOrderByOrderId/{orderId}")
	public Object viewOrderByOrderId(@PathVariable("orderId") int orderId) {
		try {
			LOGGER.info("GET /provider/viewOrderByOrderId/" + orderId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			String order = rt.exchange("http://192.168.49.2:30170/order/viewOrderByOrderId/" + orderId, HttpMethod.GET,
					entity, String.class).getBody();

//			Object order = this.restTemplate.getForObject("http://ospm-order/order/viewOrderByOrderId/" + orderId,
//					Object.class);
			return ResponseEntity.ok(order);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid Input");
		}
	}

	// Update order by orderId
	@PreAuthorize("hasRole('PROVIDER')")
	@PutMapping("/updateOrderStatusByOrderId/{orderId}")
	public ResponseEntity<?> updateOrderStatusByOrderId(@RequestBody Object jsonOrderr,
			@PathVariable("orderId") int orderId) {
		try {
			LOGGER.info("PUT /provider/updateOrderStatusByOrderId/" + orderId);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			RestTemplate rt = new RestTemplate();

			rt.exchange("http://192.168.49.2:30170/order/updateOrderStatusByOrderId/" + orderId, HttpMethod.PUT, entity,
					String.class).getBody();

//			this.restTemplate.put("http://ospm-order/order/updateOrderStatusByOrderId/" + orderId, jsonOrderr,
//					Object.class);
			return ResponseEntity.ok("Record updated");
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching data data.", HttpStatus.NOT_FOUND);
		}
	}

}
