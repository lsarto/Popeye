package com.popeyestore.repository;


import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.SubCategory;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long>{
	
	SubCategory findByName(String name);
}
