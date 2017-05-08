package com.cigarettestore.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.CigaretteToCartItem;

@Transactional
public interface CigaretteToCartItemRepository extends CrudRepository<CigaretteToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);

}
