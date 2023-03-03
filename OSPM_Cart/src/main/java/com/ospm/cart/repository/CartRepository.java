package com.ospm.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ospm.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findByUserId(int userId);
	
	@Query(value = "select * from product where product_id=:pId",nativeQuery = true)
	Object checkProductId(@Param("pId") int productId);
	
	@Query(value = "select * from user where user_id=:uId",nativeQuery = true)
	Object checkUserId(@Param("uId") int userId);
	
	@Query(value = "select * from cart where user_id=:uId and product_id=:pId",nativeQuery = true)
	Cart checkCartProductOfUser(@Param("uId") int userId, @Param("pId") int productId);
	
	@Query(value = "select product_price from product where product_id=:pId",nativeQuery = true)
	int getProductPrice(@Param("pId") int productId);
	
	@Query(value = "select cart_id from cart where user_id=:uId",nativeQuery = true)
	List<Integer> getAllCartId(@Param("uId") int userId);
	

}
