package com.popeyestore.service;

import java.util.List;

import com.popeyestore.domain.Category;

public interface CategoryService {
	
	List<Category> findAll();
	
	Category findByName(String name);

	Category createCategory(Category category);

	Category save(Category category);
}
