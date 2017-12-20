package com.popeyestore.service;

import com.popeyestore.domain.BillingAddress;
import com.popeyestore.domain.Order;
import com.popeyestore.domain.Payment;
import com.popeyestore.domain.ShippingAddress;
import com.popeyestore.domain.ShoppingCart;
import com.popeyestore.domain.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			String shippingMethod,
			User user);
	
	Order findOne(Long id);
}
