package com.cigarettestore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.domain.CigaretteToCartItem;
import com.cigarettestore.domain.User;
import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.ShoppingCart;
import com.cigarettestore.repository.CartItemRepository;
import com.cigarettestore.repository.CigaretteToCartItemRepository;
import com.cigarettestore.service.CartItemService;



@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CigaretteToCartItemRepository cigaretteToCartItemRepository;
	
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}
	
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getCigarette().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}
	
	public CartItem addCigaretteToCartItem(Cigarette cigarette, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for (CartItem cartItem : cartItemList) {
			if(cigarette.getId() == cartItem.getCigarette().getId()) {
				cartItem.setQty(cartItem.getQty()+qty);
				cartItem.setSubtotal(new BigDecimal(cigarette.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setCigarette(cigarette);
		
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(cigarette.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);
		
		CigaretteToCartItem cigaretteToCartItem = new CigaretteToCartItem();
		cigaretteToCartItem.setCigarette(cigarette);
		cigaretteToCartItem.setCartItem(cartItem);
		cigaretteToCartItemRepository.save(cigaretteToCartItem);
		
		return cartItem;
	}
	
	public CartItem findById(Long id) {
		return cartItemRepository.findOne(id);
	}
	
	public void removeCartItem(CartItem cartItem) {
		cigaretteToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}
}
