package com.popeyestore.repository;


import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>{
	
	Category findByName(String name);
}
