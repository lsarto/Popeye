package com.popeyestore.service;

import com.popeyestore.domain.BillingAddress;
import com.popeyestore.domain.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
