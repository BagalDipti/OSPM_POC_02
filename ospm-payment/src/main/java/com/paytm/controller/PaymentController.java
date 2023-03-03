package com.paytm.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.PaytmChecksum;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);


	@Autowired
	private Environment env;

	@PostMapping("/processPayment/{orderId}")
	public ResponseEntity<?> processPayment(@PathVariable int orderId ,@RequestBody Payment payment) throws Throwable {

		LOGGER.info("POST /payment/processPayment/" + orderId);
		
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("requestType", "Payment");
		body.put("mid", env.getProperty("merchantId"));
		body.put("websiteName", env.getProperty("website"));
		body.put("orderId", orderId);
		body.put("callbackUrl", env.getProperty("callbackUrl"));
		JSONObject txnAmount = new JSONObject();
		txnAmount.put("value", payment.getAmount());
		txnAmount.put("currency", "INR");
		JSONObject userInfo = new JSONObject();
		userInfo.put("custId", payment.getCustid());
		body.put("txnAmount", txnAmount);
		body.put("userInfo", userInfo);
		String checksum = PaytmChecksum.generateSignature(body.toString(), env.getProperty("merchantKey"));
		JSONObject head = new JSONObject();
		head.put("signature", checksum);
		paytmParams.put("body", body);
		paytmParams.put("head", head);

		String post_data = paytmParams.toString();
		URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid="
				+ env.getProperty("merchantId") + "&orderId=" + orderId);
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			requestWriter.close();

			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			responseData = responseReader.readLine();
			responseReader.close();
			JSONObject json = new JSONObject(responseData);
			Map m1 = new HashMap<>(json.toMap());
			
			Object obj = m1.get("body");
			
			ObjectMapper map = new ObjectMapper();
			
		    @SuppressWarnings("unchecked")
			Map<String, Object> m2 = map.convertValue(obj, Map.class);
			String TxnToken = (String) m2.get("txnToken");
            
			payment.setOrderid(orderId);
		
			return paymentoption(TxnToken,payment);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>("NOT able to reach server", HttpStatus.NOT_FOUND);

	}
	
	

	//@PostMapping("/paymentoption")
	public ResponseEntity<?> paymentoption(String txnToken, @RequestBody Payment payment) throws Throwable {
		
		LOGGER.info("POST /payment/paymentoption");
		JSONObject paytmParams = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("mid", env.getProperty("merchantId"));
		body.put("orderId", payment.getOrderid());

		body.put("returnToken", "false");
		JSONObject head = new JSONObject();
		head.put("tokenType", "TXN_TOKEN");
		head.put("token", txnToken);
		paytmParams.put("body", body);
		paytmParams.put("head", head);
		String post_data = paytmParams.toString();

		URL url = new URL("https://securegw-stage.paytm.in/theia/api/v2/fetchPaymentOptions?mid="
				+ env.getProperty("merchantId") + "&orderId=" + payment.getOrderid());
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			responseData = responseReader.readLine();
			responseReader.close();
			JSONObject json = new JSONObject(responseData);
		

			return new ResponseEntity<>(processTransaction(txnToken,payment), HttpStatus.OK);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return new ResponseEntity<>("NOT able to reach server", HttpStatus.NOT_FOUND);

	}

	//@PostMapping("/processTransaction")
	public ResponseEntity<?> processTransaction(String txnToken, @RequestBody Payment payment) throws Throwable {
		
		LOGGER.info("POST /payment/processTransaction");

		JSONObject paytmParams = new JSONObject();

		JSONObject body = new JSONObject();
		body.put("requestType", "NATIVE");
		body.put("mid", env.getProperty("merchantId"));
		body.put("orderId", payment.getOrderid());
		body.put("paymentMode", payment.getPaymentMode());
		body.put("cardInfo", "|" + payment.getCardNumber() + "|" + payment.getCvv() + "|" + payment.getExpDate());

		JSONObject head = new JSONObject();
		head.put("txnToken", txnToken);

		paytmParams.put("body", body);
		paytmParams.put("head", head);

		String post_data = paytmParams.toString();

		/* for Staging */
		URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/processTransaction?mid="
				+ env.getProperty("merchantId") + "&orderId=" + payment.getOrderid());
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			requestWriter.close();
			String responseData = "{'Total Amount':'"+payment.getAmount()+"','Order Id':'"+payment.getOrderid()+"','Payment Mode':'"+payment.getPaymentMode()+"','Status':'Payment Success'}";
			// InputStream is = connection.getInputStream();
			// BufferedReader responseReader = new BufferedReader(new
			// InputStreamReader(is));
			// responseData = responseReader.readLine();
			// responseReader.close();
			JSONObject json = new JSONObject(responseData);
			return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>("NOT able to reach server", HttpStatus.NOT_FOUND);

	}

	@PostMapping("/transactionStatus")
	public ResponseEntity<?> transactionStatus(@RequestBody Payment payment) throws Throwable {
		/* initialize an object */
		LOGGER.info("POST /payment/transactionStatus");

		JSONObject paytmParams = new JSONObject();
		/* body parameters */
		JSONObject body = new JSONObject();

		/*
		 * Find your MID in your Paytm Dashboard at
		 * https://dashboard.paytm.com/next/apikeys
		 */
		body.put("mid", env.getProperty("merchantID"));

		/* Enter your order id which needs to be check status for */
		body.put("orderId", payment.getOrderid());
		String checksum = PaytmChecksum.generateSignature(body.toString(), env.getProperty("merchantKey"));
		/* head parameters */
		JSONObject head = new JSONObject();

		/* put generated checksum value here */
		head.put("signature", checksum);

		/* prepare JSON string for request */
		paytmParams.put("body", body);
		paytmParams.put("head", head);
		String post_data = paytmParams.toString();

		/* for Staging */
		URL url = new URL("https://securegw-stage.paytm.in/v3/order/status");

		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
			requestWriter.writeBytes(post_data);
			requestWriter.close();
			String responseData = "";
			InputStream is = connection.getInputStream();
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
			responseData = responseReader.readLine();
			responseReader.close();
			JSONObject json = new JSONObject(responseData);
			return new ResponseEntity<>(json.toMap(), HttpStatus.OK);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return new ResponseEntity<>("NOT able to reach server", HttpStatus.NOT_FOUND);
	}
}
