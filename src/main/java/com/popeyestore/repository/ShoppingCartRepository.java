package com.popeyestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.ShoppingCart;


public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
