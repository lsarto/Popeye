package com.popeyestore.service;

import java.util.List;

import com.popeyestore.domain.Product;

public interface ProductService {
	List<Product> findAll();

	Product findOne(Long id);

	List<Product> findByCategory(String category);

	List<Product> blurrySearch(String name);
}
