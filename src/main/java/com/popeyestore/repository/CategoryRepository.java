package com.popeyestore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>{
		
	List<Category> findByName(String name);
}
