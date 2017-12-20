package com.popeyestore.service;

import com.popeyestore.domain.ShippingAddress;
import com.popeyestore.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
