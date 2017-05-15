package com.cigarettestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.cigarettestore.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
