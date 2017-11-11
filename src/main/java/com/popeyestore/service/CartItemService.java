package com.popeyestore.service;

import java.util.List;

import com.popeyestore.domain.Order;
import com.popeyestore.domain.CartItem;
import com.popeyestore.domain.Product;
import com.popeyestore.domain.ShoppingCart;
import com.popeyestore.domain.User;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

	CartItem updateCartItem(CartItem cartItem);

	CartItem addProductToCartItem(Product product, User user, int parseInt);

	CartItem findById(Long id);

	void removeCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);
}
