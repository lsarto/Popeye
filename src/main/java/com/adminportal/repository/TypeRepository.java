package com.adminportal.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.domain.Type;


public interface TypeRepository extends CrudRepository<Type, Long>{
		
	Type findByName(String name);
	List<Type> findAll();
}
