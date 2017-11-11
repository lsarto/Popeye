package com.popeyestore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.popeyestore.domain.Category;
import com.popeyestore.repository.CategoryRepository;
import com.popeyestore.service.CategoryService;

public class CategoryServiceImpl implements CategoryService{
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findAll() {
		return (List<Category>) categoryRepository.findAll();
	}

	@Override
	public List<Category> findByName(String name) {
		return categoryRepository.findByName(name);
	}

}
