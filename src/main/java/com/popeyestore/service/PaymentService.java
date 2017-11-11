package com.popeyestore.service;

import com.popeyestore.domain.Payment;
import com.popeyestore.domain.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
