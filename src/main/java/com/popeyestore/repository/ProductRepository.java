package com.popeyestore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findByCategory(String category);
	
	List<Product> findByNameContaining(String name);
	
}
