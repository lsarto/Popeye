package com.popeyestore.service;

import java.util.List;

import com.popeyestore.domain.Category;

public interface CategoryService {
	
	List<Category> findAll();
	
	List<Category> findByName(String name);

}
