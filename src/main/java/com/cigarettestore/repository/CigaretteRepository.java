package com.cigarettestore.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cigarettestore.domain.Cigarette;

public interface CigaretteRepository extends CrudRepository<Cigarette, Long> {
	List<Cigarette> findByCategory(String category);
	
	List<Cigarette> findByNameContaining(String name);
	
}
