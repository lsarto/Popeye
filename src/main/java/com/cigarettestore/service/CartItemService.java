package com.cigarettestore.service;

import java.util.List;

import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.domain.ShoppingCart;
import com.cigarettestore.domain.User;



public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);

	CartItem addCigaretteToCartItem(Cigarette cigarette, User user, int parseInt);
}
