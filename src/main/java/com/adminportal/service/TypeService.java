package com.adminportal.service;

import java.util.List;

import com.adminportal.domain.Category;
import com.adminportal.domain.Type;


public interface TypeService {
	
	List<Type> findAll();
	
	Type findByName(String name);

	Type createCategory(Type category, List<Category> subCategories);
	
	Type save(Type type);

	Type findOne(Long typeId);
}
