package com.popeyestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
