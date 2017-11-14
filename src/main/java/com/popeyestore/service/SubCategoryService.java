package com.popeyestore.service;

import java.util.List;

import com.popeyestore.domain.SubCategory;

public interface SubCategoryService {
	
	List<SubCategory> findAll();
	
	SubCategory findByName(String name);

	SubCategory createSubCategory(SubCategory subCategory);
}
