package com.adminportal.service;

import java.util.List;

import com.adminportal.domain.Cigarette;

public interface CigaretteService {
	
	Cigarette save(Cigarette cigarette);

	List<Cigarette> findAll();
	
	Cigarette findOne(Long id);
}
