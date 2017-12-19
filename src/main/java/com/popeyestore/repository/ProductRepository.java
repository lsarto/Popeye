package com.popeyestore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.Product;
import com.popeyestore.domain.Type;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	List<Product> findByNameContaining(String name);
	
	List<Product> findByType(Type type);
	
}
