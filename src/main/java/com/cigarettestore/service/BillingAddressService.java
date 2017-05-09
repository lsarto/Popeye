package com.cigarettestore.service;

import com.cigarettestore.domain.BillingAddress;
import com.cigarettestore.domain.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
