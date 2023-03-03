package com.ospm.order.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ospm.order.entity.Orderr;
import com.ospm.order.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Object allOrders() {
		// TODO Auto-generated method stub
		return this.orderRepository.findAll();
	}

	@Override
	public Object save(Orderr orderr) {
		// TODO Auto-generated method stub
		return this.orderRepository.save(orderr);
	}

	@Override
	public List<Object> getOrderByStatus(String orderStatus) {
		// TODO Auto-generated method stub
		return this.orderRepository.findByOrderStatus(orderStatus);
	}

	@Override
	public List<Object> getOrderByYearMonthDay(String year, String month, String day) {
		// TODO Auto-generated method stub
		LocalDate orderDate = LocalDate.parse(year+"-"+month+"-"+day);
		return this.orderRepository.findByOrderDate(orderDate);
	}

	@Override
	public int getUserIdByCartId(int cId) {
		// TODO Auto-generated method stub
		return this.orderRepository.getUserIdByCartId(cId);
	}

	@Override
	public int getProductIdByCartId(int cId) {
		// TODO Auto-generated method stub
		return this.orderRepository.getProductIdByCartId(cId);
	}

	@Override
	public int getQuantityByCartId(int cId) {
		// TODO Auto-generated method stub
		return this.orderRepository.getQuantityByCartId(cId);
	}

	@Override
	public int getPtpByCartId(int cId) {
		// TODO Auto-generated method stub
		return this.orderRepository.getPtpByCartId(cId);
	}

	@Override
	public List<Object> viewOrderByUserId(int userId) {
		// TODO Auto-generated method stub
		return this.orderRepository.findByUserId(userId);
	}

	@Override
	public Object viewOrderByOrderId(int orderId) {
		// TODO Auto-generated method stub
		return this.orderRepository.findById(orderId);
	}

	@Override
	public Object updateOrderStatusByOrderId(Orderr orderr, int orderId) {
		// TODO Auto-generated method stub
		Orderr singleOrder = this.orderRepository.getOne(orderId);
		singleOrder.setOrderStatus(orderr.getOrderStatus());
		return this.orderRepository.save(singleOrder);
	}

	@Override
	public boolean getTrueOrFalse(int userId) {
		Object user = this.orderRepository.getSingle(userId);
		if(user==null)
			return false;
		else
			return true;
	}

	@Override
	public void updateOrderById(Orderr orderr, int orderId1) {

		Orderr  orderr2 = this.orderRepository.getOne(orderId1);
		orderr2.setTotalAmount(orderr.getTotalAmount());
		orderr2.setPaymentStatus(orderr.getPaymentStatus());
		orderr2.setPaymentMode(orderr.getPaymentMode());
		this.orderRepository.save(orderr2);
	}
}
