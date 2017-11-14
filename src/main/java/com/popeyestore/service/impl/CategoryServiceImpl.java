package com.popeyestore.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.popeyestore.domain.Category;
import com.popeyestore.domain.SubCategory;
import com.popeyestore.repository.CategoryRepository;
import com.popeyestore.service.CategoryService;
import com.popeyestore.service.SubCategoryService;
import com.popeyestore.service.UserService;

@Service
public class CategoryServiceImpl implements CategoryService{
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryService subCategoryService;

	@Override
	public List<Category> findAll() {
		return (List<Category>) categoryRepository.findAll();
	}

	@Override
	public Category findByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	@Transactional
	public Category createCategory(Category category, List<SubCategory> subCategories) {
		Category localCategory = categoryRepository.findByName(category.getName());
		
		if(localCategory != null){
			LOG.info("category {} already exists. Nothing will be done.", category.getName());
		} else {
			for(SubCategory subCategory: subCategories){
				subCategoryService.createSubCategory(subCategory);
			}
			
			category.setSubCategories(subCategories);
			localCategory = categoryRepository.save(category);
		}
		
		return localCategory;
	}

}
