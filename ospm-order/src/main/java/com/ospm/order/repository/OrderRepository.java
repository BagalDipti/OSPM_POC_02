package com.ospm.order.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ospm.order.entity.Orderr;

public interface OrderRepository extends JpaRepository<Orderr, Integer>{

	List<Object> findByOrderStatus(String orderStatus);

	List<Object> findByOrderDate(LocalDate orderDate);

	@Query(value = "select user_id from cart where cart_id=:cId",nativeQuery = true)
	int getUserIdByCartId(int cId);
	
	@Query(value = "select product_id from cart where cart_id=:cId",nativeQuery = true)
	int getProductIdByCartId(int cId);
	
	@Query(value = "select quantity from cart where cart_id=:cId",nativeQuery = true)
	int getQuantityByCartId(int cId);

	@Query(value = "select product_total_price from cart where cart_id=:cId",nativeQuery = true)
	int getPtpByCartId(int cId);

	List<Object> findByUserId(int userId);
	
	@Query(value = "select * from user where user_id=:userId",nativeQuery = true)
	Object getSingle(int userId);
	
}
