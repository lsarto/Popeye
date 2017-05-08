package com.cigarettestore.service;

import com.cigarettestore.domain.UserPayment;

public interface UserPaymentService {
	UserPayment findById(Long id);
	
	void removeById(Long id);
}
