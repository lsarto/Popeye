package com.cigarettestore.service;

import java.util.List;

import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.ShoppingCart;



public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
}
