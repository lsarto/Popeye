package com.popeyestore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popeyestore.domain.SubCategory;
import com.popeyestore.repository.SubCategoryRepository;
import com.popeyestore.service.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{
	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Override
	public List<SubCategory> findByName(String name) {
		return subCategoryRepository.findByName(name);
	}

	@Override
	public List<SubCategory> findAll() {
		return (List<SubCategory>) subCategoryRepository.findAll();
	}
	

}
