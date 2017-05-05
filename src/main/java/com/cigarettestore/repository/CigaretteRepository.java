package com.cigarettestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.cigarettestore.domain.Cigarette;

public interface CigaretteRepository extends CrudRepository<Cigarette, Long> {
	
}
