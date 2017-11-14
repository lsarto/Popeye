package com.popeyestore.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popeyestore.domain.SubCategory;
import com.popeyestore.repository.SubCategoryRepository;
import com.popeyestore.service.SubCategoryService;
import com.popeyestore.service.UserService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Override
	public SubCategory findByName(String name) {
		return (SubCategory) subCategoryRepository.findByName(name);
	}

	@Override
	public List<SubCategory> findAll() {
		return (List<SubCategory>) subCategoryRepository.findAll();
	}

	@Override
	public SubCategory createSubCategory(SubCategory subCategory) {
		SubCategory localSubCategory = (SubCategory) subCategoryRepository.findByName(subCategory.getName());
		
		if(localSubCategory != null){
			LOG.info("subCategory {} already exists. Nothing will be done.", subCategory.getName());
		} else {
			localSubCategory = subCategoryRepository.save(subCategory);
		}
		
		return localSubCategory;
	}
	

}
