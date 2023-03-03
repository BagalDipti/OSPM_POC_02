package com.ospm.order.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ospm.order.entity.Order_Product;
import com.ospm.order.entity.Orderr;
import com.ospm.order.entity.Payment;
import com.ospm.order.repository.Order_ProductRepository;
import com.ospm.order.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/order")
public class OrderController {

	private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Order_ProductRepository order_ProductRepository;

	// test order controller
	@GetMapping("/test")
	public String testOrder() {
		return "order service working";
	}

	// place an order by userId

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/placeOrder/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> placeOrder(@PathVariable("userId") int userId, @RequestBody Payment payment) {
		if (this.orderService.getTrueOrFalse(userId)) {
			try {
				LOGGER.info("POST /order/placeOrder/" + userId);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<Payment> entity = new HttpEntity<Payment>(payment, headers);
				RestTemplate rt = new RestTemplate();

				List<Integer> cartIds = rt.exchange("http://192.168.49.2:30168/cart/getAllCartId/" + userId,
						HttpMethod.GET, entity, List.class).getBody();

				// List<Integer> cartIds = (List<Integer>) this.restTemplate
				// .getForObject("http://ospm-cart/cart/getAllCartId/" + userId, List.class);

				if (cartIds.size() != 0) {

					Orderr orderr = new Orderr();
					orderr.setOrderDate(LocalDate.now());
					orderr.setOrderStatus("placed");

					orderr.setUserId(userId);

					// payment service to be continue ->>)
					// orderr.setPaymentStatus("");
					// orderr.setPaymentMode("");
					Orderr order = (Orderr) this.orderService.save(orderr);

					// int orderId = order.getOrderId();
					double finalPrice = 0.0;

					for (int cId : cartIds) {

						Order_Product order_product = new Order_Product();
						// int uId = this.orderService.getUserIdByCartId(cId);
						int pId = this.orderService.getProductIdByCartId(cId);

						int qty = this.orderService.getQuantityByCartId(cId);
						int ptp = this.orderService.getPtpByCartId(cId);
						finalPrice = finalPrice + ptp;

						order_product.setProductId(pId);
						order_product.setProductTotalPrice(ptp);
						order_product.setQuantity(qty);
						order_product.setOrder(order);

						this.order_ProductRepository.save(order_product);

						rt.exchange("http://192.168.49.2:30168/cart/removeProduct/" + userId, HttpMethod.DELETE, entity,
								String.class).getBody();

						// this.restTemplate.delete("http://192.168.49.2:30168/cart/removeProduct/" +
						// userId + "/" + cId,
						// Object.class);

					}

					int orderId = order.getOrderId();

					payment.setAmount(finalPrice);

					String responseEntity = rt.exchange("http://192.168.49.2:30174/payment/processPayment/" + orderId,
							HttpMethod.POST, entity, String.class).getBody();

					// Object obj = this.restTemplate.postForObject(
					// "http://ospm-payment/payment/processPayment/" + orderId, payment,
					// Object.class);

					ObjectMapper map1 = new ObjectMapper();

					@SuppressWarnings("unchecked")
					Map<String, Object> m1 = map1.convertValue(responseEntity, Map.class);
					Object obj2 = m1.get("body");

					ObjectMapper map = new ObjectMapper();
					@SuppressWarnings("unchecked")
					Map<String, Object> m2 = map.convertValue(obj2, Map.class);
					String orderId1 = (String) m2.get("Order Id");
					String status = (String) m2.get("Status");
					String paymentMode = (String) m2.get("Payment Mode");
					String totalAmount = (String) m2.get("Total Amount");

					Orderr order4 = new Orderr();
					if (status.equals("Payment Success")) {
						System.out.println("If Block");
						order4.setOrderStatus("Placed");
						order4.setTotalAmount(Double.parseDouble(totalAmount));
						order4.setPaymentStatus(status);
						order4.setPaymentMode(paymentMode);

					} else {
						System.out.println("Else Block");
						order4.setPaymentStatus("Payment Failed.");
						order4.setTotalAmount(Double.parseDouble(totalAmount));
						order4.setPaymentMode(paymentMode);
					}

					System.out.println(order4);
					this.orderService.updateOrderById(order4, Integer.parseInt(orderId1));

					return ResponseEntity.ok(m1.get("body"));
				} else
					return new ResponseEntity<>("Please check your Cart List !!!", HttpStatus.NOT_FOUND);

			} catch (Exception e) {
				return new ResponseEntity<>("Error occurred while fetching data.", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
		}
	}

	// To concatinate list items from validCartList
	public String validList(List<Integer> validCartId) {
		String s1 = "";

		for (Integer i : validCartId)
			s1 = s1 + i + " ";

		return s1;

	}

	// To concatinate list items from inValidCartList

	public String inValidList(List<Integer> inValidCartId) {

		String s2 = "";

		for (Integer j : inValidCartId)
			s2 = s2 + j + " ";
		return s2;

	}

	// view order of a user
	@GetMapping("/viewOrderByUser/{userId}")
	public List<Object> viewOrderByUser(@PathVariable("userId") int userId) {
		LOGGER.info("GET /order/viewOrderByUser/" + userId);

		return this.orderService.viewOrderByUserId(userId);
	}

	// get all orders
	@SuppressWarnings("unchecked")
	@GetMapping("/allOrders")
	public List<Object> allOrders() {
		LOGGER.info("GET /order/allOrders");

		return (List<Object>) this.orderService.allOrders();
	}

	// get order by date
	@GetMapping("/getOrderByYearMonthDay/{year}/{month}/{day}")
	public List<Object> getOrderByYearMonthDay(@PathVariable("year") String year, @PathVariable("month") String month,
			@PathVariable("day") String day) {

		return this.orderService.getOrderByYearMonthDay(year, month, day);
	}

	// get order by order status
	@GetMapping("/getOrderByStatus/{orderStatus}")
	public List<Object> getOrderByStatus(@PathVariable("orderStatus") String orderStatus) {
		LOGGER.info("GET /order/getOrderByStatus/" + orderStatus);

		return this.orderService.getOrderByStatus(orderStatus);
	}

	// get order details by order id
	@GetMapping("/viewOrderByOrderId/{orderId}")
	public Object viewOrderByOrderId(@PathVariable("orderId") int orderId) {
		LOGGER.info("GET /order/viewOrderByOrderId/" + orderId);

		return this.orderService.viewOrderByOrderId(orderId);
	}

	// Update order by orderId
	@PutMapping("/updateOrderStatusByOrderId/{orderId}")
	public Object updateOrderStatusByOrderId(@RequestBody Orderr orderr, @PathVariable("orderId") int orderId) {
		try {
			LOGGER.info("PUT /order/updateOrderStatusByOrderId/" + orderId);

			return this.orderService.updateOrderStatusByOrderId(orderr, orderId);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while fetching data.", HttpStatus.NOT_FOUND);
		}
	}
}
