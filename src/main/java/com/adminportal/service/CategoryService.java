package com.adminportal.service;

import java.util.List;

import com.adminportal.domain.Category;
import com.adminportal.domain.Type;


public interface CategoryService {
	
	List<Category> findAll();
	
	Category findByName(String name);

	Category createCategory(Category category);
	
	List<Category> findByType(Type type);
	
	Category save(Category category);

	Category findOne(Long categoryId);
}
