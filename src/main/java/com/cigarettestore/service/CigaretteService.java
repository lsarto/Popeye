package com.cigarettestore.service;

import java.util.List;

import com.cigarettestore.domain.Cigarette;

public interface CigaretteService {
	List<Cigarette> findAll();
	
	Cigarette findOne(Long id);
}
