package com.popeyestore.service;

import java.util.List;

import com.popeyestore.domain.Type;
import com.popeyestore.domain.Category;

public interface TypeService {
	
	List<Type> findAll();
	
	Type findByName(String name);

	Type createType(Type category);
}
