package com.popeyestore.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.popeyestore.domain.CartItem;
import com.popeyestore.domain.ProductToCartItem;

@Transactional
public interface ProductToCartItemRepository extends CrudRepository<ProductToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);

}
