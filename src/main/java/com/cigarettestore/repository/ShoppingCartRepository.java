package com.cigarettestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.cigarettestore.domain.ShoppingCart;


public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
