package com.adminportal.repository;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.domain.ProductAttribute;


public interface ProductAttributeRepository extends CrudRepository<ProductAttribute, Long>{
	
}
