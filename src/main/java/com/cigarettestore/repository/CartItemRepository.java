package com.cigarettestore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.ShoppingCart;



public interface CartItemRepository extends CrudRepository<CartItem, Long>{
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
}
