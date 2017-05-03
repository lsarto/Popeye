package com.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.domain.Cigarette;

public interface CigaretteRepository extends CrudRepository<Cigarette, Long> {
	
}
