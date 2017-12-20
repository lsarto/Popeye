package com.popeyestore.repository;


import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.Type;

public interface TypeRepository extends CrudRepository<Type, Long>{
		
	Type findByName(String name);
}
