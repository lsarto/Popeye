package com.cigarettestore.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cigarettestore.domain.BillingAddress;
import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.domain.Order;
import com.cigarettestore.domain.Payment;
import com.cigarettestore.domain.ShippingAddress;
import com.cigarettestore.domain.ShoppingCart;
import com.cigarettestore.domain.User;
import com.cigarettestore.repository.OrderRepository;
import com.cigarettestore.service.CartItemService;
import com.cigarettestore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	public synchronized Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user) {
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setOrderStatus("created");
		order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			Cigarette cigarette = cartItem.getCigarette();
			cartItem.setOrder(order);
			cigarette.setInStockNumber(cigarette.getInStockNumber() - cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUser(user);
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOne(Long id) {
		return orderRepository.findOne(id);
	}

}
