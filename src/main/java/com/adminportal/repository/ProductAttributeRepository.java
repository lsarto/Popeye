package com.adminportal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.domain.Product;
import com.adminportal.domain.ProductAttribute;


public interface ProductAttributeRepository extends CrudRepository<ProductAttribute, Long>{
	void deleteById(Long id);
	
	List<ProductAttribute> findByProduct(Product product);
}
