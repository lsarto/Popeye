package com.popeyestore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.SubCategory;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long>{
	
	List<SubCategory> findByName(String name);
}
