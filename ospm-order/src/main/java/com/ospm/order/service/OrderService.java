package com.ospm.order.service;

import java.util.List;

import com.ospm.order.entity.Orderr;

public interface OrderService {

	Object allOrders();

	Object save(Orderr orderr);

	List<Object> getOrderByStatus(String orderStatus);

	List<Object> getOrderByYearMonthDay(String year, String month, String day);

	int getUserIdByCartId(int cId);

	int getProductIdByCartId(int cId);

	int getQuantityByCartId(int cId);

	int getPtpByCartId(int cId);

	List<Object> viewOrderByUserId(int userId);

	Object viewOrderByOrderId(int orderId);

	Object updateOrderStatusByOrderId(Orderr orderr, int orderId);

	boolean getTrueOrFalse(int userId);

	void updateOrderById(Orderr orderr, int orderId1);


}
