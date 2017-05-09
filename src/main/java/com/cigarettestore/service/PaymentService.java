package com.cigarettestore.service;

import com.cigarettestore.domain.Payment;
import com.cigarettestore.domain.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
