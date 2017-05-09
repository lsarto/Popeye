package com.cigarettestore.service;

import com.cigarettestore.domain.ShippingAddress;
import com.cigarettestore.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
